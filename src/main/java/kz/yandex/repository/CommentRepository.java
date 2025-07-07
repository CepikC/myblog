package kz.yandex.repository;

public interface CommentRepository {

    void addComment(Long postId, String text);

    void deleteComment(Long commentId);

    void updateComment(Long commentId, String text);
}
