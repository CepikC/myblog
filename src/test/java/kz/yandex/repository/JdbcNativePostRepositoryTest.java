package kz.yandex.repository;

import kz.yandex.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
public class JdbcNativePostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    private Long postId;

    @BeforeEach
    void init() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");

        postId = postRepository.save("Test", "Some text", null, List.of("tag1", "tag2"));
    }

    @Test
    void testFindPosts() {
        List<Post> posts = postRepository.findPosts(10, 0);
        assertEquals(1, posts.size());
        assertEquals("Test", posts.get(0).getTitle());
    }

    @Test
    void testFindPostsByTag() {
        List<Post> posts = postRepository.findPostsByTag("tag1", 10, 0);
        assertEquals(1, posts.size());
    }

    @Test
    void testFindById() {
        Post post = postRepository.findById(postId);
        assertNotNull(post);
        assertEquals("Test", post.getTitle());
        assertEquals(2, post.getTags().size());
    }

    @Test
    void testUpdateWithImagePath() {
        postRepository.update(postId, "Updated", "Updated Text", "base64string", List.of("newtag"));
        Post updated = postRepository.findById(postId);
        assertEquals("Updated", updated.getTitle());
        assertEquals("Updated Text", updated.getText());
        assertEquals(1, updated.getTags().size());
    }

    @Test
    void testUpdateWithoutImagePath() {
        postRepository.update(postId, "No Image", "Only text", null, List.of("solo"));
        Post post = postRepository.findById(postId);
        assertEquals("No Image", post.getTitle());
        assertEquals("Only text", post.getText());
    }


    @Test
    void testDeletePost() {
        postRepository.delete(postId);
        List<Post> posts = postRepository.findPosts(10, 0);
        assertTrue(posts.isEmpty());
    }
}
