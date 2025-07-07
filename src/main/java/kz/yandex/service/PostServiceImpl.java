package kz.yandex.service;

import kz.yandex.model.Post;
import kz.yandex.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Long save(String title, String text, String tags, MultipartFile image) {
        String imageBase64 = null;

        if (image != null && !image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                imageBase64 = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка чтения изображения", e);
            }
        }

        List<String> tagList = Arrays.stream(tags.split("\\s+"))
                .filter(s -> !s.isBlank())
                .toList();

        return postRepository.save(title, text, imageBase64, tagList);
    }

    @Override
    public void update(Long id, String title, String text, String tags, MultipartFile image) {
        String imageBase64 = null;

        if (image != null && !image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                imageBase64 = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка чтения изображения", e);
            }
        }

        List<String> tagList = Arrays.stream(tags.split("\\s+"))
                .filter(s -> !s.isBlank())
                .toList();

        postRepository.update(id, title, text, imageBase64, tagList);
    }

    @Override
    public List<Post> getPosts(String search, int pageSize, int pageNumber) {
        int offset = (pageNumber - 1) * pageSize;
        if (search.isBlank()) {
            return postRepository.findPosts(pageSize, offset);
        }
        return postRepository.findPostsByTag(search, pageSize, offset);
    }

    @Override
    public boolean hasNext(String search, int pageSize, int pageNumber) {
        int offset = pageSize * pageNumber;
        return search.isBlank()
                ? !postRepository.findPosts(pageSize, offset).isEmpty()
                : !postRepository.findPostsByTag(search, pageSize, offset).isEmpty();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(id);
    }

}

