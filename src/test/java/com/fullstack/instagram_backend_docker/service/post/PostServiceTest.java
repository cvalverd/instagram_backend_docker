package com.fullstack.instagram_backend_docker.service.post;

import com.fullstack.instagram_backend_docker.api.request.PostCreateRequest;
import com.fullstack.instagram_backend_docker.model.Post;
import com.fullstack.instagram_backend_docker.repository.PostRepository;
import com.fullstack.instagram_backend_docker.service.profileinfo.ProfileInfoService;
import com.fullstack.instagram_backend_docker.model.ProfileInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ProfileInfoService profileInfoService;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setup() {
        postService = new PostServiceImpl(postRepository, profileInfoService);
    }

    @Test
    void savePost() {
        // Arrange
        PostCreateRequest request = PostCreateRequest.builder()
                .caption("Test post")
                .imageUrl("https://example.com/image.jpg")
                .profileInfoId(1L)
                .build();

        when(profileInfoService.findProfileInfoById(request.getProfileInfoId()))
                .thenReturn(Optional.of(ProfileInfo.builder().id(1L).build()));

        Post expected = Post.builder()
                .caption(request.getCaption())
                .imageUrl(request.getImageUrl())
                .profileInfo(expectedProfileInfo())
                .createdAt(LocalDateTime.now())
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(expected);

        // Act
        Post savedPost = postService.savePost(request);

        // Assert
        assertEquals(expected, savedPost);
    }

    private ProfileInfo expectedProfileInfo() {
        return ProfileInfo.builder().id(1L).build();
    }

    @Test
    void findPostById() {
        // Arrange
        Post post = Post.builder()
                .id(1L)
                .caption("Test post")
                .imageUrl("https://example.com/image.jpg")
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act
        Optional<Post> foundPost = postService.findPostById(1L);

        // Assert
        assertTrue(foundPost.isPresent());
        assertEquals(post, foundPost.get());
    }

    @Test
    void findPostsByProfileInfoId() {
        // Arrange
        Post post1 = Post.builder()
                .id(1L)
                .caption("Test post 1")
                .imageUrl("https://example.com/image1.jpg")
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .caption("Test post 2")
                .imageUrl("https://example.com/image2.jpg")
                .build();

        List<Post> posts = List.of(post1, post2);

        when(postRepository.findByProfileInfoId(1L)).thenReturn(posts);

        // Act
        List<Post> foundPosts = postService.findPostsByProfileInfoId(1L);

        // Assert
        assertEquals(posts, foundPosts);
    }

    @Test
    void findAllPosts() {
        // Arrange
        Post post1 = Post.builder()
                .id(1L)
                .caption("Test post 1")
                .imageUrl("https://example.com/image1.jpg")
                .build();

        Post post2 = Post.builder()
                .id(2L)
                .caption("Test post 2")
                .imageUrl("https://example.com/image2.jpg")
                .build();

        List<Post> posts = List.of(post1, post2);

        when(postRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> foundPosts = postService.findAllPosts();

        // Assert
        assertEquals(posts, foundPosts);
    }

    @Test
    void deletePostById() {
        // Arrange
        doNothing().when(postRepository).deleteById(1L);

        // Act
        postService.deletePostById(1L);

        // Assert
        verify(postRepository).deleteById(1L);
    }
}