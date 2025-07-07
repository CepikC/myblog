package kz.yandex.repository;

public interface LikeRepository {

    void updateLike(Long postId, int delta);
}
