package com.example.book.service;

import com.example.book.domain.Comments;
import com.example.book.domain.Posts;
import com.example.book.dto.CommentsSaveData;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.errors.PostsNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("댓글 관리")
class CommentServiceTest {
    private static final String TITLE = "테스트 제목";
    private static final String CONTENT = "테스트 내용";
    private static final String AUTHOR = "책 저자";
    private static final String EMAIL = "test@gmail.com";
    private static final String POST_CONTENT = "게시글 내용";
    private static final String COMMENT_CONTENT = "댓글 내용";

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Posts posts;

    @BeforeEach
    public void setPost(){
        posts = postService.save(getPost());
    }

    @AfterEach
    public void cleanUp(){
        postService.delete(posts.getId());
    }

    @Nested
    @DisplayName("댓글 작성은")
    class Discribe_save {
        @Nested
        @DisplayName("댓글을 입력받으면")
        class Context_when_comment {
            CommentsSaveData saveData;

            @BeforeEach
            public void setUp() throws Exception {
                saveData = getComment(posts.getId());
            }
            @Test
            @DisplayName("저장하고 댓글을 리턴한다.")
            void it_return_comment() {
                Comments comment = commentService.save(saveData);

                assertThat(comment.getPostId()).isEqualTo(saveData.getPostId());
                assertThat(comment.getEmail()).isEqualTo(saveData.getEmail());
                assertThat(comment.getContent()).isEqualTo(saveData.getContent());
            }
        }

        @Nested
        @DisplayName("없는 게시물에 댓글을 작성하면")
        class Context_when_none_post {
            CommentsSaveData commentsSaveData;

            @BeforeEach
            public void setUp(){
                commentsSaveData = getComment(-1L);
            }
            @Test
            @DisplayName("찾을 수 없는 게시물이라는 예외를 던진다.")
            void it_throw_PostsNotFoundException() {
                assertThatThrownBy(() -> commentService.save(commentsSaveData))
                        .isInstanceOf(PostsNotFoundException.class);
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
                .title(TITLE)
                .content(POST_CONTENT)
                .author(AUTHOR)
                .email("test@gmail.com")
                .build();
    }
}