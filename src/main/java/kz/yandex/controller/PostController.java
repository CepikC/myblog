package kz.yandex.controller;

import kz.yandex.model.Post;
import kz.yandex.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/posts")
class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String redirectRoot() {
        return "redirect:/posts";
    }

    @PostMapping
    public String addPost(@RequestParam("title") String title,
                          @RequestParam("text") String text,
                          @RequestParam("tags") String tags,
                          @RequestParam("image") MultipartFile image) {
        Long id = postService.save(title, text, tags, image);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}")
    public String editPost(@PathVariable("id") Long id,
                           @RequestParam("title") String title,
                           @RequestParam("text") String text,
                           @RequestParam("tags") String tags,
                           @RequestParam(value = "image", required = false) MultipartFile image) {
        postService.update(id, title, text, tags, image);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }

    @GetMapping
    public String listPosts(@RequestParam(name = "search", defaultValue = "") String search,
                            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                            Model model) {

        List<Post> posts = postService.getPosts(search, pageSize, pageNumber);
        boolean hasNext = postService.hasNext(search, pageSize, pageNumber);

        model.addAttribute("posts", posts);
        model.addAttribute("search", search);
        model.addAttribute("paging", new Paging(pageNumber, pageSize, hasNext, pageNumber > 1));

        return "posts";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);

        if (post == null || post.getImagePath() == null || post.getImagePath().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] imageBytes = Base64.getDecoder().decode(post.getImagePath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // или .IMAGE_JPEG в зависимости от оригинального формата
            headers.setContentLength(imageBytes.length);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable("id") Long postId,
                             @RequestParam("text") String text) {
        postService.addComment(postId, text);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{commentId}")
    public String updateComment(@PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId,
                                @RequestParam("text") String text) {
        postService.updateComment(postId, commentId, text);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId) {
        postService.deleteComment(postId, commentId);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable("id") Long postId,
                           @RequestParam("like") boolean like) {
        postService.updateLikeCount(postId, like);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/add")
    public String addPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "add-post";
    }

    @GetMapping("/{id}/edit")
    public String editPostForm(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "add-post"; // или "post-edit", если у тебя другой шаблон
    }


    public static class Paging {
        private final int pageNumber;
        private final int pageSize;
        private final boolean hasNext;
        private final boolean hasPrevious;

        public Paging(int pageNumber, int pageSize, boolean hasNext, boolean hasPrevious) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.hasNext = hasNext;
            this.hasPrevious = hasPrevious;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public boolean hasPrevious() {
            return hasPrevious;
        }
    }
}
