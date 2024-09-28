package com.fullstack.instagram_backend_docker.repository;

import com.fullstack.instagram_backend_docker.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // [MethodName_StateUnderTest_ExpectedBehavior]
    @Test
    public void FindByUsername_FindValidUserByUsername_UserExistsOnDatabase() {

        // Arrange
        User user = User.builder()
                .name("Armando Casas")
                .username("armandocasas")
                .email("U8kS6@example.com")
                .password("123456")
                .birthdate(LocalDate.of(1996, 10, 10))
                .profileInfo(null)
                .build();

        userRepository.save(user);

        // Act
        User foundUser = userRepository.findByUsername("armandocasas");

        // Assert
        assertNotNull(foundUser.getId());
        assertEquals("Armando Casas", foundUser.getName());
    }

    @Test
    public void FindByEmail_FindValidUserByEmail_UserExistsOnDatabase() {

        // Arrange
        User user = User.builder()
                .name("Armando Casas")
                .username("armandocasas")
                .email("U8kS6@example.com")
                .password("123456")
                .birthdate(LocalDate.of(1996, 10, 10))
                .profileInfo(null)
                .build();

        userRepository.save(user);

        // Act
        User foundUser = userRepository.findByEmail("U8kS6@example.com");

        // Assert
        assertNotNull(foundUser.getId());
        assertEquals("Armando Casas", foundUser.getName());
    }

}
