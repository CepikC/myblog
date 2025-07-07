package kz.yandex.service;

public interface CommentService {
    void addComment(Long postId, String text);

    void deleteComment(Long postId, Long commentId);

    void updateComment(Long postId, Long commentId, String text);
}
