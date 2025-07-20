package kz.yandex.service;

import kz.yandex.repository.LikeRepository;
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
public class LikeServiceImplTest {

    @MockBean
    private LikeRepository likeRepository;

    @Autowired
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
