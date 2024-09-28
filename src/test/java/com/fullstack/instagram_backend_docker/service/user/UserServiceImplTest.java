package com.fullstack.instagram_backend_docker.service.user;

import com.fullstack.instagram_backend_docker.api.request.UserUpdateRequest;
import com.fullstack.instagram_backend_docker.model.User;
import com.fullstack.instagram_backend_docker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id(1L)
                .name("Armando Casas")
                .username("armandocasas")
                .email("armandocasas@gmail.com")
                .password("123456")
                .birthdate(LocalDate.of(1996, 10, 10))
                .profileInfo(null)
                .build();
    }

    @Test
    void saveUser() {
        // Arrange
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertEquals(user, savedUser);
        verify(userRepository).save(user);
    }

    @Test
    void findUserById() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.findUserById(user.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository).findById(user.getId());
    }

    @Test
    void findUserByUsername() {
        // Arrange
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        // Act
        User foundUser = userService.findUserByUsername(user.getUsername());

        // Assert
        assertEquals(user, foundUser);
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void findUserByEmail() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // Act
        User foundUser = userService.findUserByEmail(user.getEmail());

        // Assert
        assertEquals(user, foundUser);
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void updateUser() {
        // Arrange
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .username("Armando Casas")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User updatedUser = userService.updateUser(user.getId(), updateRequest);

        // Assert
        assertEquals(user, updatedUser);
        verify(userRepository).findById(user.getId());
        verify(userRepository).save(user);
    }

    @Test
    void findAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act
        List<User> foundUsers = userService.findAllUsers();

        // Assert
        assertEquals(List.of(user), foundUsers);
        verify(userRepository).findAll();
    }

    @Test
    void deleteUserById() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user)); // Mock findById to return the user
        doNothing().when(userRepository).deleteById(user.getId()); // Mock deleteById to do nothing

        // Act
        userService.deleteUserById(user.getId());

        // Assert
        verify(userRepository).deleteById(user.getId());
    }
}