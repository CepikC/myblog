package kz.yandex.repository;

import kz.yandex.model.Post;
import java.util.List;

public interface PostRepository {

    Long save(String title, String text, String imagePath, List<String> tags);

    void update(Long id, String title, String text, String imagePath, List<String> tags);

    void delete(Long id);

    List<Post> findPosts(int limit, int offset);

    List<Post> findPostsByTag(String tag, int limit, int offset);

    Post findById(Long id);

    void addComment(Long postId, String text);

    void updateLike(Long postId, int delta);

    void deleteComment(Long commentId);

    void updateComment(Long commentId, String text);
}
