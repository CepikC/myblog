package kz.yandex.service;

import kz.yandex.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public void updateLikeCount(Long postId, boolean like) {
        likeRepository.updateLike(postId, like ? 1 : -1);
    }

}
