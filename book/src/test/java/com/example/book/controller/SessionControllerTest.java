package com.example.book.controller;

import com.example.book.domain.User;
import com.example.book.dto.UserLoginData;
import com.example.book.dto.UserSaveRequestData;
import com.example.book.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("세션 요청")
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final String TOKEN_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";

    @BeforeEach
    void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8",true))
                .build();

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Nested
    @DisplayName("로그인 요청은")
    class Describe_login{
        private User user;

        @BeforeEach
        void setUp() throws Exception{
            userRepository.deleteAll();;
            user =  prepareUser(getUserSaveData());
        }

        @Nested
        @DisplayName("올바른 사용자 정보가 주어지면")
        class Context_with_user{
            private UserLoginData userLoginData;

            @BeforeEach
            void prepareData(){
                userLoginData = userLoginData.builder()
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build();
            }

            @Test
            @DisplayName("토큰과 201 Created HTTP 상태코드를 응답한다.")
            void it_resposne_created_with_token() throws Exception{
                mockMvc.perform(post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .contentType(objectMapper.writeValueAsString(userLoginData)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.accessToken", matchesPattern(TOKEN_REGEX)));
            }
        }

        @Nested
        @DisplayName("존재하지 않은 사용자 정보가 주어지면")
        class Context_with_wrong_User {
            private UserLoginData userLoginWrongData;

            @BeforeEach
            void setLoginData(){
                userLoginWrongData = UserLoginData.builder()
                        .email("worng" + user.getEmail())
                        .password(user.getPassword())
                        .build();
            }

            @Test
            @DisplayName("BadRequest 400을 응답한다.")
            void it_response_notFoundException() throws Exception{
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .contentType(objectMapper.writeValueAsString(userLoginWrongData)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호가 주어지면")
        class Context_With_wrong_password {
            private UserLoginData userLoginWrongData;

            @BeforeEach
            void setLoginData() {
                userLoginWrongData = UserLoginData.builder()
                        .email(user.getEmail())
                        .password(user.getPassword() + "x")
                        .build();
            }

            @Test
            @DisplayName("BadRequest 400을 응답한다.")
            void it_response_status_400() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userLoginWrongData)))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    private UserSaveRequestData getUserSaveData() {
        return UserSaveRequestData.builder()
                .email("test@email.com")
                .name("테스트 이름")
                .password("1234abcd*")
                .build();
    }


    private User prepareUser(UserSaveRequestData saveRequestData) throws Exception {
        ResultActions actions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveRequestData)));

        MvcResult mvcResult = actions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(content, User.class);
    }
}