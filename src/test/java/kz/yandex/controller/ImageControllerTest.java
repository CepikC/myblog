package kz.yandex.controller;

import kz.yandex.configuration.DataSourceConfiguration;
import kz.yandex.configuration.WebConfiguration;
import kz.yandex.model.Post;
import kz.yandex.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = {
        DataSourceConfiguration.class,
        WebConfiguration.class,
        ImageControllerTest.TestConfig.class
})
@WebAppConfiguration
@TestPropertySource("classpath:test-application.properties")
public class ImageControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public ImageController imageController() {
            return new ImageController(imageService());
        }

        @Bean
        public ImageService imageService() {
            return mock(ImageService.class);
        }
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ImageService imageService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetImage_Success() throws Exception {
        byte[] imageBytes = "dummy-image-bytes".getBytes();
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setImagePath(encodedImage);

        when(imageService.getPostById(1L)).thenReturn(mockPost);

        mockMvc.perform(get("/posts/images/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageBytes));

        verify(imageService, atLeastOnce()).getPostById(1L);
    }

    @Test
    void testGetImage_NotFound() throws Exception {
        when(imageService.getPostById(999L)).thenReturn(null);

        mockMvc.perform(get("/posts/images/999"))
                .andExpect(status().isNotFound());

        verify(imageService, times(1)).getPostById(999L);
    }

    @Test
    void testGetImage_EmptyPath() throws Exception {
        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setImagePath("");

        when(imageService.getPostById(1L)).thenReturn(mockPost);

        mockMvc.perform(get("/posts/images/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetImage_BadBase64() throws Exception {
        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setImagePath("!!!badbase64");

        when(imageService.getPostById(1L)).thenReturn(mockPost);

        mockMvc.perform(get("/posts/images/1"))
                .andExpect(status().isInternalServerError());
    }
}
