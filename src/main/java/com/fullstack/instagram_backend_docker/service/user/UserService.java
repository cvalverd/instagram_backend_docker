package com.fullstack.instagram_backend_docker.service.user;

import java.util.List;
import java.util.Optional;

import com.fullstack.instagram_backend_docker.api.request.UserUpdateRequest;
import com.fullstack.instagram_backend_docker.model.User;

public interface UserService {
    User saveUser(User user);
    Optional<User> findUserById(Long id);
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User updateUser(Long id, UserUpdateRequest updateRequest);
    List<User> findAllUsers();
    void deleteUserById(Long id);
}
