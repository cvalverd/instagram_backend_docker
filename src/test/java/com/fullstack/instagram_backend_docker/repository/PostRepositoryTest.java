package com.fullstack.instagram_backend_docker.repository;

import com.fullstack.instagram_backend_docker.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    // [MethodName_StateUnderTest_ExpectedBehavior]
    @Test
    public void FindByProfileInfoId_FindAllPostsByProfileInfoId_PostsExistsOnDatabase() {

        // Arrange
        Post post1 = Post.builder()
                .caption("Test post 1")
                .imageUrl("https://example.com/image1.jpg")
                .profileInfo(null)
                .createdAt(LocalDateTime.now())
                .build();

        Post post2 = Post.builder()
                .caption("Test post 2")
                .imageUrl("https://example.com/image2.jpg")
                .profileInfo(null)
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        // Act
        List<Post> foundPosts = postRepository.findByProfileInfoId(null);

        // Assert
        assertNotNull(foundPosts);
        assertEquals(2, foundPosts.size());
    }

}
