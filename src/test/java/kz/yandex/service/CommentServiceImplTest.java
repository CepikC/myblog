package kz.yandex.service;

import kz.yandex.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.profiles.active=test")
public class CommentServiceImplTest {
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddComment() {
        commentService.addComment(3L, "Nice!");
        verify(commentRepository).addComment(3L, "Nice!");
    }

    @Test
    void testDeleteComment() {
        commentService.deleteComment(1L, 100L);
        verify(commentRepository).deleteComment(100L);
    }

    @Test
    void testUpdateComment() {
        commentService.updateComment(1L, 200L, "Updated");
        verify(commentRepository).updateComment(200L, "Updated");
    }
}
