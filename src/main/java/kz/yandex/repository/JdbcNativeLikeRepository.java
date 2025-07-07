package kz.yandex.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcNativeLikeRepository implements LikeRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeLikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void updateLike(Long postId, int delta) {
        jdbcTemplate.update(
                "UPDATE posts SET likes_count = likes_count + ? WHERE id = ?",
                delta, postId
        );
    }
}
