package com.fullstack.instagram_backend_docker.service.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fullstack.instagram_backend_docker.api.request.PostCreateRequest;
import com.fullstack.instagram_backend_docker.exceptionhandler.ResourceNotFoundException;
import com.fullstack.instagram_backend_docker.model.Post;
import com.fullstack.instagram_backend_docker.repository.PostRepository;
import com.fullstack.instagram_backend_docker.service.profileinfo.ProfileInfoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;
    private final ProfileInfoService profileInfoService;

    @Override
    public Post savePost(PostCreateRequest postRequest) {
        logger.debug("Saving post with request: {} - method savePost", postRequest);
        Long profileInfoId = postRequest.getProfileInfoId();
        if (profileInfoId != null) {
            Post post = Post.builder()
                    .caption(postRequest.getCaption())
                    .imageUrl(postRequest.getImageUrl())
                    .createdAt(LocalDateTime.now())
                    .profileInfo(profileInfoService.findProfileInfoById(profileInfoId)
                            .orElseThrow(() -> new ResourceNotFoundException("ProfileInfo not found")))
                    .build();
            Post savedPost = postRepository.save(post);
            logger.debug("Post saved successfully. Post ID: {} - method savePost", savedPost.getId());
            return savedPost;
        } else {
            logger.error("ProfileInfoId is required - method savePost");
            throw new IllegalArgumentException("ProfileInfoId is required");
        }
    }

    @Override
    public Optional<Post> findPostById(Long id) {
        logger.debug("Getting post by id: {} - method findPostById", id);
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            logger.debug("Post found: id={}, caption={} - method findPostById", post.get().getId(), post.get().getCaption());
        } else {
            logger.debug("Post not found by id: {} - method findPostById", id);
        }
        return post;
    }

    @Override
    public List<Post> findPostsByProfileInfoId(Long profileInfoId) {
        logger.debug("Getting all posts by profile info id: {} - method findPostsByProfileInfoId", profileInfoId);
        List<Post> posts = postRepository.findByProfileInfoId(profileInfoId);
        logger.debug("Posts found: {} - method findPostsByProfileInfoId", posts.size());
        return posts;
    }

    @Override
    public List<Post> findAllPosts() {
        logger.debug("Getting all posts - method findAllPosts");
        List<Post> posts = postRepository.findAll();
        logger.debug("Posts found: {} - method findAllPosts", posts.size());
        return posts;
    }

    @Override
    public void deletePostById(Long id) {
        logger.debug("Deleting post by id: {} - method deletePostById", id);
        try {
            postRepository.deleteById(id);
            logger.debug("Post deleted successfully - method deletePostById");
        } catch (Exception e) {
            logger.error("Post not found by id: {} - method deletePostById", id);
            throw new ResourceNotFoundException("Post not found");
        }
    }
}
