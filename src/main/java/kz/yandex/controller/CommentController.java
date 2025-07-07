package kz.yandex.controller;

import kz.yandex.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/posts")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable("id") Long postId,
                             @RequestParam("text") String text) {
        commentService.addComment(postId, text);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{commentId}")
    public String updateComment(@PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId,
                                @RequestParam("text") String text) {
        commentService.updateComment(postId, commentId, text);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(postId, commentId);
        return "redirect:/posts/" + postId;
    }
}
