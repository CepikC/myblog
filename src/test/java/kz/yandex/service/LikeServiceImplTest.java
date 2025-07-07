package kz.yandex.service;

import kz.yandex.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class LikeServiceImplTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeServiceImpl likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateLikeCount() {
        likeService.updateLikeCount(4L, true);
        verify(likeRepository).updateLike(4L, 1);

        likeService.updateLikeCount(4L, false);
        verify(likeRepository).updateLike(4L, -1);
    }
}
