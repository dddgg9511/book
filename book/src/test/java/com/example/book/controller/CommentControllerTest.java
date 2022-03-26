package com.example.book.controller;

import com.example.book.domain.Comments;
import com.example.book.domain.Posts;
import com.example.book.dto.CommentsSaveData;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.errors.PostsNotFoundException;
import com.example.book.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("댓글 관리")
class CommentControllerTest {
    private static final String EMAIL = "test@gmail.com";
    private static final String COMMENT_CONTENT = "댓글 내용";

    Posts posts;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PostService postService;

    @BeforeEach
    void setPosts() throws Exception {
        posts = postService.save(getPost());
    }

    @Nested
    @DisplayName("댓글 등록 요청")
    class Describe_post {
        @Nested
        @DisplayName("댓글 정보가 주어지면")
        class Context_with_comment {
            CommentsSaveData saveData;

            @BeforeEach
            public void setUp() throws Exception {
                saveData = getComment(posts.getId());
            }

            @Test
            @DisplayName("댓글을 저장하고 상태코드 Created를 응답한다.")
            void it_return_status_created() throws Exception {
                mockMvc.perform(post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(saveData)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.email",is(saveData.getEmail())))
                        .andExpect(jsonPath("$.content",is(saveData.getContent())));
            }
        }

        @Nested
        @DisplayName("없는 게시물에 댓글을 작성하면")
        class Context_when_none_post {
            CommentsSaveData saveData;

            @BeforeEach
            public void setUp(){
                saveData = getComment(-1L);
            }
            @Test
            @DisplayName("찾을 수 없는 게시물이라는 예외를 던진다.")
            void it_throw_PostsNotFoundException() throws Exception {
                mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveData)))
                        .andExpect(status().isNotFound())
                        .andExpect((result ->
                                assertThat(result.getResolvedException().getClass().getCanonicalName())
                                        .isEqualTo(PostsNotFoundException.class.getCanonicalName())));
            }
        }
    }

    @Nested
    @DisplayName("댓글 목록 조회")
    class Describe_comment_list {
        @Nested
        @DisplayName("게시물에 댓글이 존재하면")
        class Context_when_exist_comments {
            private static final int count = 10;

            @BeforeEach
            public void setUp() throws Exception {
                for(int i = 0; i < count; i++){
                    mockMvc.perform(post("/comments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getComment(posts.getId()))));
                }
            }

            @Test
            @DisplayName("댓글 목록을 리턴한다.")
            void return_comment_list() throws Exception {
                mockMvc.perform(get("/comments/" + posts.getId()))
                                .andExpect(status().isOk());

            }
        }
    }



    private CommentsSaveData getComment(Long id) {
        return CommentsSaveData.builder()
                .postId(id)
                .content(COMMENT_CONTENT)
                .email(EMAIL)
                .build();
    }

    private PostsSaveRequestData getPost() {
        return PostsSaveRequestData.builder()
                .title("테스트 제목")
                .content("게시물 내용")
                .author("AUTHOR")
                .email("test@email.com")
                .build();
    }
}