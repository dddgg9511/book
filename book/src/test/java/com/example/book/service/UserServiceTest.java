package com.example.book.service;

import com.example.book.domain.User;
import com.example.book.dto.UserSaveRequestData;
import com.example.book.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void signUp() throws Exception{
        UserSaveRequestData saveRequestData = createUser();
        User user = createUser().toEntity();

        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(user, "id", fakeUserId);

        when(userRepository.save(any())).thenReturn(user);

        User savedUser = userService.signUp(saveRequestData);

        assertEquals(saveRequestData.getName(), savedUser.getName());
        assertEquals(saveRequestData.getEmail(), savedUser.getEmail());
    }

    private UserSaveRequestData createUser(){
        String name = "choo";
        String email = "choo@naver.com";
        String password = "password";

        return UserSaveRequestData.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}