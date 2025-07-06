package kz.yandex.service;


import kz.yandex.model.Post;
import kz.yandex.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_withImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "dummy data".getBytes());

        when(postRepository.save(any(), any(), any(), any())).thenReturn(1L);

        Long id = postService.save("Title", "Text", "tag1 tag2", image);

        assertEquals(1L, id);
        verify(postRepository).save(eq("Title"), eq("Text"), anyString(), eq(List.of("tag1", "tag2")));
    }

    @Test
    void testSave_withoutImage() {
        when(postRepository.save(any(), any(), any(), any())).thenReturn(2L);

        Long id = postService.save("Title", "Text", "tag1", null);

        assertEquals(2L, id);
        verify(postRepository).save(eq("Title"), eq("Text"), isNull(), eq(List.of("tag1")));
    }

    @Test
    void testUpdate_withImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "img".getBytes());

        postService.update(3L, "New Title", "New Text", "tagX tagY", image);

        verify(postRepository).update(eq(3L), eq("New Title"), eq("New Text"), anyString(), eq(List.of("tagX", "tagY")));
    }

    @Test
    void testUpdate_withoutImage() {
        postService.update(3L, "New Title", "New Text", "tagX tagY", null);

        verify(postRepository).update(eq(3L), eq("New Title"), eq("New Text"), isNull(), eq(List.of("tagX", "tagY")));
    }

    @Test
    void testGetPosts_withSearch() {
        when(postRepository.findPostsByTag(eq("test"), anyInt(), anyInt())).thenReturn(List.of());

        List<Post> posts = postService.getPosts("test", 5, 1);
        assertNotNull(posts);

        verify(postRepository).findPostsByTag(eq("test"), eq(5), eq(0));
    }

    @Test
    void testGetPostById() {
        Post mockPost = new Post();
        when(postRepository.findById(10L)).thenReturn(mockPost);

        Post post = postService.getPostById(10L);
        assertEquals(mockPost, post);
    }

    @Test
    void testDelete() {
        postService.delete(5L);
        verify(postRepository).delete(5L);
    }

    @Test
    void testAddComment() {
        postService.addComment(3L, "Nice!");
        verify(postRepository).addComment(3L, "Nice!");
    }

    @Test
    void testUpdateLikeCount() {
        postService.updateLikeCount(4L, true);
        verify(postRepository).updateLike(4L, 1);

        postService.updateLikeCount(4L, false);
        verify(postRepository).updateLike(4L, -1);
    }

    @Test
    void testDeleteComment() {
        postService.deleteComment(1L, 100L);
        verify(postRepository).deleteComment(100L);
    }

    @Test
    void testUpdateComment() {
        postService.updateComment(1L, 200L, "Updated");
        verify(postRepository).updateComment(200L, "Updated");
    }
}
