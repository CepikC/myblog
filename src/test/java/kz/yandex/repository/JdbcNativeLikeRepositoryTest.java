package kz.yandex.repository;

import kz.yandex.configuration.DataSourceConfiguration;
import kz.yandex.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativeLikeRepository.class, JdbcNativePostRepository.class})
@TestPropertySource("classpath:test-application.properties")
public class JdbcNativeLikeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    private Long postId;

    @BeforeEach
    void init() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");

        postId = postRepository.save("Test", "Some text", null, List.of("tag1", "tag2"));
    }

    @Test
    void testUpdateLike() {
        likeRepository.updateLike(postId, 1);
        Post post = postRepository.findById(postId);
        assertEquals(1, post.getLikesCount());
    }
}
