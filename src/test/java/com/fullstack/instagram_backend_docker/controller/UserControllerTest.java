package com.fullstack.instagram_backend_docker.controller;

import com.fullstack.instagram_backend_docker.api.request.UserUpdateRequest;
import com.fullstack.instagram_backend_docker.model.User;
import com.fullstack.instagram_backend_docker.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("updatedUser");
        updateRequest.setEmail("updated@example.com");
    }

    @Test
    void createUser() {
        // Arrange
        when(userService.saveUser(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.createUser(user);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        EntityModel<User> savedUserModel = response.getBody();
        assertNotNull(savedUserModel);
        assertEquals(user.getId(), savedUserModel.getContent().getId());
        assertTrue(savedUserModel.hasLink("self"));
        verify(userService).saveUser(any(User.class));
    }

    @Test
    void getUserById_found() {
        // Arrange
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<EntityModel<User>> response = userController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EntityModel<User> foundUserModel = response.getBody();
        assertNotNull(foundUserModel);
        assertEquals(user.getId(), foundUserModel.getContent().getId());
        assertTrue(foundUserModel.hasLink("self"));
        verify(userService).findUserById(1L);
    }

    @Test
    void getUserById_notFound() {
        // Arrange
        when(userService.findUserById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<EntityModel<User>> response = userController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).findUserById(1L);
    }

    @Test
    void getUserByUsername_found() {
        // Arrange
        when(userService.findUserByUsername("testuser")).thenReturn(user);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.getUserByUsername("testuser");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EntityModel<User> foundUserModel = response.getBody();
        assertNotNull(foundUserModel);
        assertEquals(user.getUsername(), foundUserModel.getContent().getUsername());
        assertTrue(foundUserModel.hasLink("self"));
        verify(userService).findUserByUsername("testuser");
    }

    @Test
    void getUserByUsername_notFound() {
        // Arrange
        when(userService.findUserByUsername("testuser")).thenReturn(null);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.getUserByUsername("testuser");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).findUserByUsername("testuser");
    }

    @Test
    void getUserByEmail_found() {
        // Arrange
        when(userService.findUserByEmail("test@example.com")).thenReturn(user);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.getUserByEmail("test@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EntityModel<User> foundUserModel = response.getBody();
        assertNotNull(foundUserModel);
        assertEquals(user.getEmail(), foundUserModel.getContent().getEmail());
        assertTrue(foundUserModel.hasLink("self"));
        verify(userService).findUserByEmail("test@example.com");
    }

    @Test
    void getUserByEmail_notFound() {
        // Arrange
        when(userService.findUserByEmail("test@example.com")).thenReturn(null);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.getUserByEmail("test@example.com");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).findUserByEmail("test@example.com");
    }

    @Test
    void patchUser() {
        // Arrange
        when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(user);

        // Act
        ResponseEntity<EntityModel<User>> response = userController.patchUser(1L, updateRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EntityModel<User> updatedUserModel = response.getBody();
        assertNotNull(updatedUserModel);
        assertEquals(user.getId(), updatedUserModel.getContent().getId());
        assertTrue(updatedUserModel.hasLink("self"));
        verify(userService).updateUser(eq(1L), any(UserUpdateRequest.class));
    }

    @Test
    void getAllUsers() {
        // Arrange
        List<User> userList = List.of(user);
        when(userService.findAllUsers()).thenReturn(userList);

        // Act
        ResponseEntity<CollectionModel<EntityModel<User>>> response = userController.getAllUsers();
        CollectionModel<EntityModel<User>> foundUsersModel = response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(foundUsersModel);
        assertEquals(1, foundUsersModel.getContent().size());
        assertTrue(foundUsersModel.hasLink("self"));
        verify(userService).findAllUsers();
    }

    @Test
    void deleteUser() {
        // Arrange
        doNothing().when(userService).deleteUserById(1L);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUserById(1L);
    }
}
