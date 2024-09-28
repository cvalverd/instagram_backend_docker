package com.fullstack.instagram_backend_docker.controller;

import com.fullstack.instagram_backend_docker.api.request.PostCreateRequest;
import com.fullstack.instagram_backend_docker.model.Post;
import com.fullstack.instagram_backend_docker.model.ProfileInfo;
import com.fullstack.instagram_backend_docker.repository.PostRepository;
import com.fullstack.instagram_backend_docker.repository.ProfileInfoRepository;
import com.fullstack.instagram_backend_docker.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private Post post;

    @BeforeEach
    void setup() {
        post = Post.builder()
                .id(1L)
                .caption("Test post")
                .imageUrl("https://example.com/image.jpg")
                .profileInfo(ProfileInfo.builder().id(1L).build())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createPost() {
        // Arrange
        PostCreateRequest request = PostCreateRequest.builder()
                .caption(post.getCaption())
                .imageUrl(post.getImageUrl())
                .profileInfoId(post.getProfileInfo().getId())
                .build();

        when(postService.savePost(any(PostCreateRequest.class))).thenReturn(post);

        // Act
        ResponseEntity<EntityModel<Post>> response = postController.createPost(request);
        EntityModel<Post> savedPostModel = response.getBody();

        // Assert
        assertNotNull(savedPostModel);
        assertEquals(post, savedPostModel.getContent());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(savedPostModel.hasLink("self"));
    }

    @Test
    void getPostById() {
        // Arrange
        when(postService.findPostById(post.getId())).thenReturn(Optional.of(post));

        // Act
        ResponseEntity<EntityModel<Post>> response = postController.getPostById(post.getId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EntityModel<Post> returnedPostModel = response.getBody();
        assertNotNull(returnedPostModel);
        assertEquals(post, returnedPostModel.getContent());
        assertTrue(returnedPostModel.hasLink("self"));
    }

    @Test
    void getPostsByProfileInfoId() {
        // Arrange
        when(postService.findPostsByProfileInfoId(post.getProfileInfo().getId())).thenReturn(List.of(post));

        // Act
        ResponseEntity<CollectionModel<EntityModel<Post>>> response = postController.getPostsByProfileInfoId(post.getProfileInfo().getId());
        CollectionModel<EntityModel<Post>> foundPostsModel = response.getBody();

        // Assert
        assertNotNull(foundPostsModel);
        assertEquals(1, foundPostsModel.getContent().size());
        assertTrue(foundPostsModel.hasLink("self"));
    }

    @Test
    void getAllPosts() {
        // Arrange
        when(postService.findAllPosts()).thenReturn(List.of(post));

        // Act
        ResponseEntity<CollectionModel<EntityModel<Post>>> response = postController.getAllPosts();
        CollectionModel<EntityModel<Post>> foundPostsModel = response.getBody();

        // Assert
        assertNotNull(foundPostsModel);
        assertEquals(1, foundPostsModel.getContent().size());
        assertTrue(foundPostsModel.hasLink("self"));
    }

    @Test
    void deletePost() {
        // Arrange
        doNothing().when(postService).deletePostById(post.getId());

        // Act
        ResponseEntity<Void> response = postController.deletePost(post.getId());

        // Assert
        verify(postService).deletePostById(post.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}