package com.example.book.controller;

import com.example.book.domain.Posts;
import com.example.book.domain.User;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.dto.SessionResponseData;
import com.example.book.dto.UserLoginData;
import com.example.book.dto.UserSaveRequestData;
import com.example.book.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("게시물 관리")
class PostControllerTest {
    private static final String TITLE = "테스트 제목";
    private static final String CONTENT = "테스트 내용";
    private static final String AUTHOR = "테스트 저자";
    private static final String EMAIL = "test@email.com";
    private static final String NEW_TITLE = "새로운 제목";
    private static final String NEW_CONTENT = "새로운 내용";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext wac;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user;

    private SessionResponseData sessionResponseData;


    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        objectMapper.registerModule(new JavaTimeModule());
        userRepository.deleteAll();

        user = prepareUser(getUserSaveData());

        sessionResponseData = login(getLoginData(user));
    }

    @Nested
    @DisplayName("게시물 목록 조회 요청은")
    class Describe_GET_List{
        @Nested
        @DisplayName("등록된 게시물이 있다면")
        class Context_exist_root{
            final int postCount = 10;
            List<Posts> postsList = new ArrayList<>();

            @BeforeEach
            void setUp() throws Exception{
                for(int i = 0; i < postCount; i++){
                    Posts posts = preparePost(getPostSaveData());
                    postsList.add(getPostSaveData().toEntity());
                }
            }

            @Test
            @DisplayName("전체 리스트를 리턴한다.")
            void it_return_list() throws Exception{
                ResultActions resultActions = mockMvc.perform(get("/posts"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TITLE)));
            }
        }

        @Nested
        @DisplayName("등록된 게시물이 없다면")
        class Context_not_exist_post{
            private static final String EMPTY_LIST = "[]";

            @BeforeEach
            void setEmptyList() throws Exception{
                List<Posts> postList = objectMapper.convertValue(
                        getPostList(),
                        new TypeReference<List<Posts>>() {
                    });

                postList.forEach(posts -> {
                    try {
                        deletePostBeforeTest(posts.getId());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }

            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void it_return_empty_list() throws Exception{
                mockMvc.perform(get("/posts"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(EMPTY_LIST)));
            }
        }
    }

    private User prepareUser(UserSaveRequestData saveRequestData) throws Exception {
        ResultActions actions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveRequestData)));

        MvcResult mvcResult = actions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(content, User.class);
    }

    private SessionResponseData login(UserLoginData userLoginData) throws Exception {
        ResultActions actions = mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginData)));

        MvcResult mvcResult = actions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(content, SessionResponseData.class);
    }

    private UserLoginData getLoginData(User user) {
        return UserLoginData.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    private UserSaveRequestData getUserSaveData() {
        return UserSaveRequestData.builder()
                .email("test@email.com")
                .name("테스트이름")
                .password("password123*")
                .build();
    }

    private PostsSaveRequestData getPostSaveData() {
        return PostsSaveRequestData.builder()
                .title(TITLE)
                .content(CONTENT)
                .author(AUTHOR)
                .email(EMAIL)
                .build();
    }

    private Posts preparePost(PostsSaveRequestData saveRequestData) throws Exception {
        ResultActions actions = mockMvc.perform(post("/posts")
                .content(objectMapper.writeValueAsString(saveRequestData))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + sessionResponseData.getAccessToken())
        );

        MvcResult mvcResult = actions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(content, Posts.class);
    }

    private List<Posts> getPostList() throws Exception{
        ResultActions actions = mockMvc.perform(get("/posts"));

        MvcResult mvcResult = actions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(content, List.class);
    }

    private void deletePostBeforeTest(Long id) throws Exception {
        mockMvc.perform(delete("/posts/" + id)
                .header("Authorization",
                        "Bearer " + sessionResponseData.getAccessToken()));
    }
}