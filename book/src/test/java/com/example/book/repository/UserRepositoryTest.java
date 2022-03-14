package com.example.book.repository;

import com.example.book.domain.BaseEntity;
import com.example.book.domain.User;
import com.example.book.domain.UserRole;
import com.example.book.errors.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    String email = "choo@naver.com";
    String password = "test";
    UserRole role = UserRole.USER;

    @BeforeEach
    void setUp(){
        userRepository.save(User.builder()
                .email(email)
                .password(password)
                .role(role).build());
    }

    @Test
    void findByEmail(){
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new UserNotFoundException(email));

    }

    @Test
    void save(){
        String saveEmail = "choo";
        User user = User.builder()
                .email(saveEmail)
                .password(password)
                .role(role).build();

        final User savedUser = userRepository.save(user);

        assertEquals(user.getEmail(), saveEmail);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getRole(), role);
    }
}