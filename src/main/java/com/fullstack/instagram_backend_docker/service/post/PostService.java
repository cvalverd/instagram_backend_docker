package com.fullstack.instagram_backend_docker.service.post;

import java.util.List;
import java.util.Optional;

import com.fullstack.instagram_backend_docker.api.request.PostCreateRequest;
import com.fullstack.instagram_backend_docker.model.Post;

public interface PostService {
   Post savePost(PostCreateRequest post);
   Optional<Post> findPostById(Long id);
   List<Post> findPostsByProfileInfoId(Long profileInfoId);
   List<Post> findAllPosts();
   void deletePostById(Long id);
}
