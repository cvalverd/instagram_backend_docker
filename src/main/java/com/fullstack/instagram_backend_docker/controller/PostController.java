package com.fullstack.instagram_backend_docker.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;


import com.fullstack.instagram_backend_docker.api.request.PostCreateRequest;
import com.fullstack.instagram_backend_docker.model.Post;
import com.fullstack.instagram_backend_docker.service.post.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    @PostMapping()
    public ResponseEntity<EntityModel<Post>> createPost(@RequestBody PostCreateRequest postRequest) {
        logger.info("Creating a new post with request: {}", postRequest);
        Post savedPost = postService.savePost(postRequest);
        logger.info("Post created successfully. Post ID: {}", savedPost.getId());

        EntityModel<Post> postModel = EntityModel.of(savedPost);
        postModel.add(linkTo(methodOn(PostController.class).getPostById(savedPost.getId())).withSelfRel());

        return ResponseEntity.created(linkTo(methodOn(PostController.class).getPostById(savedPost.getId())).toUri())
                .body(postModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Post>> getPostById(@PathVariable Long id) {
        logger.info("Getting post by id: {}", id);
        Optional<Post> post = postService.findPostById(id);
        if (post.isPresent()) {
            EntityModel<Post> postModel = EntityModel.of(post.get());
            postModel.add(linkTo(methodOn(PostController.class).getPostById(id)).withSelfRel());

            return ResponseEntity.ok(postModel);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profile-info/{profileInfoId}")
    public ResponseEntity<CollectionModel<EntityModel<Post>>> getPostsByProfileInfoId(@PathVariable Long profileInfoId) {
        logger.info("Getting all posts by profile info id: {}", profileInfoId);
        List<EntityModel<Post>> posts = postService.findPostsByProfileInfoId(profileInfoId).stream()
                .map(post -> {
                    EntityModel<Post> postModel = EntityModel.of(post);
                    postModel.add(linkTo(methodOn(PostController.class).getPostById(post.getId())).withSelfRel());
                    return postModel;
                }).toList();

        return ResponseEntity.ok(CollectionModel.of(posts,
                linkTo(methodOn(PostController.class).getPostsByProfileInfoId(profileInfoId)).withSelfRel()));
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<Post>>> getAllPosts() {
        logger.info("Getting all posts");
        List<EntityModel<Post>> posts = postService.findAllPosts().stream()
                .map(post -> {
                    EntityModel<Post> postModel = EntityModel.of(post);
                    postModel.add(linkTo(methodOn(PostController.class).getPostById(post.getId())).withSelfRel());
                    return postModel;
                }).toList();

        return ResponseEntity.ok(CollectionModel.of(posts,
                linkTo(methodOn(PostController.class).getAllPosts()).withSelfRel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        logger.info("Deleting post by id: {}", id);
        postService.deletePostById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}