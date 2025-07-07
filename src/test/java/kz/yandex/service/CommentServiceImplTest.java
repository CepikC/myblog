package kz.yandex.service;

import kz.yandex.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
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
