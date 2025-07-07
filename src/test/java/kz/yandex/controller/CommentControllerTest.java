package kz.yandex.controller;

import kz.yandex.configuration.DataSourceConfiguration;
import kz.yandex.configuration.WebConfiguration;
import kz.yandex.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = {
        DataSourceConfiguration.class,
        WebConfiguration.class,
        CommentControllerTest.TestConfig.class
})
@WebAppConfiguration
@TestPropertySource("classpath:test-application.properties")
class CommentControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public CommentController commentController() {
            return new CommentController(commentService());
        }

        @Bean
        public CommentService commentService() {
            return mock(CommentService.class);
        }
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CommentService commentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

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
