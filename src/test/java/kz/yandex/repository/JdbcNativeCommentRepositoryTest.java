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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativeCommentRepository.class, JdbcNativePostRepository.class})
@TestPropertySource("classpath:test-application.properties")
public class JdbcNativeCommentRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Long postId;

    @BeforeEach
    void init() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");

        postId = postRepository.save("Test", "Some text", null, List.of("tag1", "tag2"));
    }

    @Test
    void testAddComment() {
        commentRepository.addComment(postId, "New comment");
        Post post = postRepository.findById(postId);
        assertEquals(1, post.getComments().size());
        assertEquals("New comment", post.getComments().get(0).getText());
    }

    @Test
    void testUpdateComment() {
        commentRepository.addComment(postId, "Old");
        Long commentId = jdbcTemplate.queryForObject("SELECT id FROM comments WHERE post_id = ?", Long.class, postId);
        commentRepository.updateComment(commentId, "Updated comment");
        Post post = postRepository.findById(postId);
        assertEquals("Updated comment", post.getComments().get(0).getText());
    }

    @Test
    void testDeleteComment() {
        commentRepository.addComment(postId, "Will be deleted");
        Long commentId = jdbcTemplate.queryForObject("SELECT id FROM comments WHERE post_id = ?", Long.class, postId);
        commentRepository.deleteComment(commentId);
        Post post = postRepository.findById(postId);
        assertTrue(post.getComments().isEmpty());
    }
}
