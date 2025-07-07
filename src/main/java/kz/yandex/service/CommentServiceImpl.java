package kz.yandex.service;

import kz.yandex.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void addComment(Long postId, String text) {
        commentRepository.addComment(postId, text);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        commentRepository.deleteComment(commentId);
    }

    @Override
    public void updateComment(Long postId, Long commentId, String text) {
        commentRepository.updateComment(commentId, text);
    }
}
