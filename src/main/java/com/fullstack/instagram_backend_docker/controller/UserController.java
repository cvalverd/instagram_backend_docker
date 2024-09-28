package com.fullstack.instagram_backend_docker.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.instagram_backend_docker.api.request.UserUpdateRequest;
import com.fullstack.instagram_backend_docker.model.User;
import com.fullstack.instagram_backend_docker.service.user.UserService;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<EntityModel<User>> createUser(@RequestBody User user) {
        logger.info("Creating a new user with request: {}", user);
        User savedUser = userService.saveUser(user);
        logger.info("User created successfully. User ID: {}", savedUser.getId());

        EntityModel<User> userModel = EntityModel.of(savedUser);
        userModel.add(linkTo(methodOn(UserController.class).getUserById(savedUser.getId())).withSelfRel());

        return ResponseEntity.created(
                        linkTo(methodOn(UserController.class).getUserById(savedUser.getId())).toUri())
                .body(userModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUserById(@PathVariable Long id) {
        logger.info("Getting a user by ID: {}", id);
        Optional<User> user = userService.findUserById(id);
        if (user.isPresent()) {
            EntityModel<User> userModel = EntityModel.of(user.get());
            userModel.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());

            return ResponseEntity.ok(userModel);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<EntityModel<User>> getUserByUsername(@PathVariable String username) {
        logger.info("Getting a user by username: {}", username);
        User user = userService.findUserByUsername(username);
        if (user != null) {
            EntityModel<User> userModel = EntityModel.of(user);
            userModel.add(linkTo(methodOn(UserController.class).getUserByUsername(username)).withSelfRel());

            return ResponseEntity.ok(userModel);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EntityModel<User>> getUserByEmail(@PathVariable String email) {
        logger.info("Getting a user by email: {}", email);
        User user = userService.findUserByEmail(email);
        if (user != null) {
            EntityModel<User> userModel = EntityModel.of(user);
            userModel.add(linkTo(methodOn(UserController.class).getUserByEmail(email)).withSelfRel());

            return ResponseEntity.ok(userModel);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<User>> patchUser(@PathVariable Long id, @RequestBody UserUpdateRequest updateRequest) {
        logger.info("Updating a user with ID: {} and request: {}", id, updateRequest);
        User updatedUser = userService.updateUser(id, updateRequest);

        EntityModel<User> userModel = EntityModel.of(updatedUser);
        userModel.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());

        return ResponseEntity.ok(userModel);
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers() {
        logger.info("Getting all users");
        List<EntityModel<User>> users = userService.findAllUsers().stream()
                .map(user -> {
                    EntityModel<User> userModel = EntityModel.of(user);
                    userModel.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
                    return userModel;
                }).toList();

        return ResponseEntity.ok(CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Deleting a user with ID: {}", id);
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

