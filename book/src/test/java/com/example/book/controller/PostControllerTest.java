package com.example.book.controller;

import com.example.book.domain.Posts;
import com.example.book.domain.User;
import com.example.book.dto.*;
import com.example.book.errors.InvalidParameterException;
import com.example.book.errors.PostsNotFoundException;
import com.example.book.repository.PostRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("????????? ??????")
class PostControllerTest {
    private static final String TITLE = "????????? ??????";
    private static final String CONTENT = "????????? ??????";
    private static final String AUTHOR = "????????? ??????";
    private static final String EMAIL = "test@email.com";
    private static final String NEW_TITLE = "????????? ??????";
    private static final String NEW_CONTENT = "????????? ??????";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

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
    @DisplayName("????????? ?????? ?????? ?????????")
    class Describe_GET_List{
        @Nested
        @DisplayName("????????? ???????????? ?????????")
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
            @DisplayName("?????? ???????????? ????????????.")
            void it_return_list() throws Exception{
                ResultActions resultActions = mockMvc.perform(get("/posts"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(TITLE)));
            }
        }

        @Nested
        @DisplayName("????????? ???????????? ?????????")
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
            @DisplayName("??? ???????????? ????????????.")
            void it_return_empty_list() throws Exception{
                mockMvc.perform(get("/posts"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(EMPTY_LIST)));
            }
        }
    }

    @Nested
    @DisplayName("????????? ?????? ?????? ?????????")
    class Describe_GET{
        private Posts post;

        @BeforeEach
        void setUp() throws Exception{
            post = preparePost(getPostSaveData());
        }

        @Nested
        @DisplayName("id??? ???????????? ???????????? ?????????")
        class Context_exist_id_post{

            @DisplayName("????????? ????????? 200 ok HTTP ??????????????? ????????????.")
            @Test
            void it_reponse_post() throws Exception{
                mockMvc.perform(get("/posts/" + post.getId()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title", is(post.getTitle())))
                        .andExpect(jsonPath("$.content", is(post.getContent())))
                        .andExpect(jsonPath("$.author", is(post.getAuthor())));
            }
        }

        @Nested
        @DisplayName("id??? ???????????? ???????????? ?????????")
        class Context_when_post_is_not_exist {

            @BeforeEach
            public void setUp(){
                postRepository.deleteAll();
            }

            @DisplayName("???????????? ?????? ??? ????????? ????????? ?????????.")
            @Test
            void it_throw_postNotFoundException() throws Exception {
                mockMvc.perform(get("/posts/" + post.getId()))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(
                                (result) -> assertTrue(
                                        result.getResolvedException().getClass().isAssignableFrom(PostsNotFoundException.class)));

            }
        }
    }

    @Nested
    @DisplayName("????????? ?????? ?????????")
    class Describe_post {
        private PostsSaveRequestData postsSaveRequestData;

        @Nested
        @DisplayName("????????? ????????? ???????????????")
        class Context_with_new_post {
            @BeforeEach
            public void setUp(){
                postsSaveRequestData = getPostSaveData();
            }

            @Test
            @DisplayName("???????????? ???????????? ???????????? Created??? ????????????.")
            void it_return_status_created() throws Exception{
                mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postsSaveRequestData)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.author",is(postsSaveRequestData.getAuthor())))
                        .andExpect(jsonPath("$.content",is(postsSaveRequestData.getContent())))
                        .andExpect(jsonPath("$.title",is(postsSaveRequestData.getTitle())));
            }
        }

        @Nested
        @DisplayName("????????? ????????? ????????? ???????????????")
        class Context_with_worng_post{
            @BeforeEach
            public void setUp() {
                postsSaveRequestData = PostsSaveRequestData.builder().build();
            }

            @Test
            @DisplayName("???????????? Bad Request??? ????????????.")
            void it_return_status_bad_request() throws Exception{
                mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postsSaveRequestData)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(
                                (result) -> assertEquals(
                                        InvalidParameterException.class.getCanonicalName(),
                                        result.getResolvedException().getClass().getCanonicalName()
                                ));
            }
        }
    }

    @Nested
    @DisplayName("????????? ?????? ?????????")
    class Describe_patch{
        PostUpdateRequestData updateData;

        @BeforeEach
        public void setUp(){
            updateData = PostUpdateRequestData.builder()
                    .title(NEW_TITLE)
                    .content(NEW_CONTENT).build();
        }
        @Nested
        @DisplayName("???????????? ???????????????")
        class Context_exist_posts{
            Posts post;

            @BeforeEach
            public void setUp() throws Exception {
                post = preparePost(getPostSaveData());
            }

            @Test
            @DisplayName("???????????? ???????????? ????????? ???????????? ????????????,")
            public void it_return_posts() throws Exception{
                mockMvc.perform(patch("/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                        .andDo(print())
                        .andExpect(jsonPath("$.title", is(updateData.getTitle())))
                        .andExpect(jsonPath("$.content", is(updateData.getContent())));
            }
        }

        @Nested
        @DisplayName("???????????? ???????????? ?????? ??????")
        class Context_none_posts{
            @BeforeEach
            public void setUp(){
                postRepository.deleteAll();
            }

            @Test
            @DisplayName("???????????? ?????? ??? ????????? ????????? ?????????.")
            public void it_return() throws Exception{
                mockMvc.perform(patch("/posts/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect((result ->
                                assertThat(result.getResolvedException().getClass().getCanonicalName())
                                        .isEqualTo(PostsNotFoundException.class.getCanonicalName())));

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
                .name("???????????????")
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