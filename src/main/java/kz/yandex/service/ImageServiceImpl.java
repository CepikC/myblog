package kz.yandex.service;

import kz.yandex.model.Post;
import kz.yandex.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private final PostRepository postRepository;

    @Autowired
    public ImageServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id);
    }
}
