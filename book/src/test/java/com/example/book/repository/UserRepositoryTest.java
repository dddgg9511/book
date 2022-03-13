package com.example.book.repository;

import com.example.book.domain.User;
import com.example.book.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void save(){
        String email = "choo@naver.com";
        String password = "test";
        UserRole role = UserRole.USER;

        User user = User.builder()
                .email(email)
                .password(password)
                .role(role).build();

        final User savedUser = userRepository.save(user);

        assertEquals(user.getEmail(), email);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getRole(), role);
    }
}