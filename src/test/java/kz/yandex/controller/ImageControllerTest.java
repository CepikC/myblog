package kz.yandex.controller;

import kz.yandex.model.Post;
import kz.yandex.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class ImageControllerTest {

    @MockBean
    private ImageService imageService;

    @Autowired
    private MockMvc mockMvc;

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
