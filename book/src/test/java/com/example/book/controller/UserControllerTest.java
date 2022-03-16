package com.example.book.controller;

import com.example.book.domain.User;
import com.example.book.dto.UserSaveRequestData;
import com.example.book.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void signUp() throws Exception{
        UserSaveRequestData 박성은 = UserSaveRequestData.builder()
                .name("박성은")
                .password("123")
                .email("bbung@naver.com").build();

        when(userService.signUp(any())).thenReturn(박성은.toEntity());


        mockMvc.perform(post("/user",박성은))
                .andExpect(status().isOk());
    }
}