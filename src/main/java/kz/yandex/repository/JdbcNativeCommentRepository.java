package kz.yandex.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addComment(Long postId, String text) {
        jdbcTemplate.update(
                "INSERT INTO comments (post_id, text) VALUES (?, ?)",
                postId, text
        );
    }

    @Override
    public void deleteComment(Long commentId) {
        jdbcTemplate.update("DELETE FROM comments WHERE id = ?", commentId);
    }

    @Override
    public void updateComment(Long commentId, String text) {
        jdbcTemplate.update("UPDATE comments SET text = ? WHERE id = ?", text, commentId);
    }
}
