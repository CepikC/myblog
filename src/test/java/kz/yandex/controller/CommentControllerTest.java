package kz.yandex.controller;

import kz.yandex.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddComment() throws Exception {
        mockMvc.perform(post("/posts/5/comments")
                        .param("text", "Great post!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/5"));

        verify(commentService, times(1)).addComment(5L, "Great post!");
    }

    @Test
    void testUpdateComment() throws Exception {
        mockMvc.perform(post("/posts/5/comments/9")
                        .param("text", "Updated comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/5"));

        verify(commentService, times(1)).updateComment(5L, 9L, "Updated comment");
    }

    @Test
    void testDeleteComment() throws Exception {
        mockMvc.perform(post("/posts/5/comments/9/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/5"));

        verify(commentService, times(1)).deleteComment(5L, 9L);
    }
}
