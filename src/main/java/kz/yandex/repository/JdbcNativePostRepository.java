package kz.yandex.repository;

import kz.yandex.model.Comment;
import kz.yandex.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findPosts(int limit, int offset) {
        return jdbcTemplate.query(
                "SELECT * FROM posts ORDER BY id DESC LIMIT ? OFFSET ?",
                new Object[]{limit, offset},
                (rs, rowNum) -> {
                    Long postId = rs.getLong("id");

                    List<String> tags = jdbcTemplate.queryForList(
                            "SELECT tag FROM tags WHERE post_id = ?",
                            new Object[]{postId},
                            String.class
                    );

                    List<Comment> comments = jdbcTemplate.query(
                            "SELECT id, text FROM comments WHERE post_id = ?",
                            new Object[]{postId},
                            (crs, i) -> new Comment(
                                    crs.getLong("id"),
                                    crs.getString("text")
                            )
                    );

                    return new Post(
                            postId,
                            rs.getString("title"),
                            rs.getString("text"),
                            rs.getString("image_path"),
                            rs.getInt("likes_count"),
                            tags,
                            comments
                    );
                }
        );
    }


    @Override
    public List<Post> findPostsByTag(String tag, int limit, int offset) {
        return jdbcTemplate.query(
                """
                        SELECT DISTINCT p.* FROM posts p
                        JOIN tags t ON p.id = t.post_id
                        WHERE LOWER(t.tag) LIKE LOWER(?)
                        ORDER BY p.id DESC
                        LIMIT ? OFFSET ?
                        """,
                new Object[]{"%" + tag + "%", limit, offset},
                (rs, rowNum) -> {
                    Long postId = rs.getLong("id");

                    List<String> tags = jdbcTemplate.queryForList(
                            "SELECT tag FROM tags WHERE post_id = ?",
                            new Object[]{postId},
                            String.class
                    );

                    List<Comment> comments = jdbcTemplate.query(
                            "SELECT id, text FROM comments WHERE post_id = ?",
                            new Object[]{postId},
                            (crs, i) -> new Comment(
                                    crs.getLong("id"),
                                    crs.getString("text")
                            )
                    );

                    return new Post(
                            postId,
                            rs.getString("title"),
                            rs.getString("text"),
                            rs.getString("image_path"),
                            rs.getInt("likes_count"),
                            tags,
                            comments
                    );
                }
        );
    }

    @Override
    public Post findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM posts WHERE id = ?",
                (rs, rowNum) -> {
                    List<String> tags = jdbcTemplate.queryForList(
                            "SELECT tag FROM tags WHERE post_id = ?",
                            new Object[]{id},
                            String.class
                    );

                    List<Comment> comments = jdbcTemplate.query(
                            "SELECT id, text FROM comments WHERE post_id = ?",
                            new Object[]{id},
                            (crs, i) -> new Comment(
                                    crs.getLong("id"),
                                    crs.getString("text")
                            )
                    );

                    return new Post(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("text"),
                            rs.getString("image_path"),
                            rs.getInt("likes_count"),
                            tags,
                            comments
                    );
                },
                id
        );
    }

    @Override
    public Long save(String title, String text, String imagePath, List<String> tags) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO posts (title, text, image_path, likes_count) VALUES (?, ?, ?, 0)",
                    new String[]{"id"}
            );
            ps.setString(1, title);
            ps.setString(2, text);
            ps.setString(3, imagePath);
            return ps;
        }, keyHolder);

        Long postId = ((Number) keyHolder.getKeys().get("id")).longValue();

        for (String tag : tags) {
            jdbcTemplate.update("INSERT INTO tags (post_id, tag) VALUES (?, ?)", postId, tag);
        }

        return postId;
    }

    @Override
    public void update(Long id, String title, String text, String imagePath, List<String> tags) {
        if (imagePath != null) {
            jdbcTemplate.update(
                    "UPDATE posts SET title = ?, text = ?, image_path = ? WHERE id = ?",
                    title, text, imagePath, id
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE posts SET title = ?, text = ? WHERE id = ?",
                    title, text, id
            );
        }

        jdbcTemplate.update("DELETE FROM tags WHERE post_id = ?", id);
        for (String tag : tags) {
            jdbcTemplate.update("INSERT INTO tags (post_id, tag) VALUES (?, ?)", id, tag);
        }
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    }


    public void addComment(Long postId, String text) {
        jdbcTemplate.update(
                "INSERT INTO comments (post_id, text) VALUES (?, ?)",
                postId, text
        );
    }

    public void updateLike(Long postId, int delta) {
        jdbcTemplate.update(
                "UPDATE posts SET likes_count = likes_count + ? WHERE id = ?",
                delta, postId
        );
    }

    public void deleteComment(Long commentId) {
        jdbcTemplate.update("DELETE FROM comments WHERE id = ?", commentId);
    }

    public void updateComment(Long commentId, String text) {
        jdbcTemplate.update("UPDATE comments SET text = ? WHERE id = ?", text, commentId);
    }

}



