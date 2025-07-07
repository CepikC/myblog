package kz.yandex.service;

import kz.yandex.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    Long save(String title, String text, String tags, MultipartFile image);

    void update(Long id, String title, String text, String tags, MultipartFile image);

    List<Post> getPosts(String search, int pageSize, int pageNumber);

    boolean hasNext(String search, int pageSize, int pageNumber);

    Post getPostById(Long id);

    void delete(Long id);


}
