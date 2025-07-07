package kz.yandex.service;

public interface LikeService {

    void updateLikeCount(Long postId, boolean like);
}
