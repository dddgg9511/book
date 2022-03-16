package com.example.book.controller;

import com.example.book.domain.User;
import com.example.book.dto.UserSaveRequestData;
import com.example.book.repository.UserRepository;
import com.example.book.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    MockMvc mockMvc;

    private static final String NAME = "테스트 이름";
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "1234abcd*";

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("사용자 회원가입 요청은")
    class Describe_signUp {
        UserSaveRequestData userSaveRequestData;

        @Nested
        @DisplayName("사용자 회원가입 정보가 주어진다면")
        class Context_with_user_data {
            @BeforeEach
            void setUp() {
                userSaveRequestData = getUserSaveData("new");
            }

            @Test
            @DisplayName("사용자를 저장하고 Created를 응답한다.")
            void it_response_status_created() throws Exception {
                mockMvc.perform(post("/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userSaveRequestData)))
                        .andExpect(status().isCreated())
                        .andDo(print());
            }
        }
    }

    private UserSaveRequestData getUserSaveData(String prefix) {
        return UserSaveRequestData.builder()
                .email(prefix + EMAIL)
                .name(NAME)
                .password(PASSWORD)
                .build();
    }
}