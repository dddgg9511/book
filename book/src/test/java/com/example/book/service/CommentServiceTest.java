package com.example.book.service;

import com.example.book.domain.Comments;
import com.example.book.domain.Posts;
import com.example.book.dto.CommentsSaveData;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.errors.CommentNotFoundException;
import com.example.book.errors.PostsNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
    private static final String COMMENT_NEW_TEXT = "새로운 댓글 내용";

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
    //미스터 추 입술위에 추 달콤하게 추 몸이 떨려
    @Nested
    @DisplayName("댓글 수정은")
    class Describe_update{
        @Nested
        @DisplayName("없는 댓글이면")
        class Context_when_InvaludComments{
            @Test
            @DisplayName("없는 게시물이라는 예외를 던진다")
            public void it_throw_InvalidCommentException(){
                assertThatThrownBy(() ->commentService.update(-1L, "est"))
                        .isInstanceOf(CommentNotFoundException.class);

            }
        }

        @Nested
        @DisplayName("댓글이 존재하면")
        class Context_when_exist_comments{
            Comments comments;

            @BeforeEach
            public void setUp(){
               comments = commentService.save(getComment(posts.getId()));
            }

            @Test
            @DisplayName("댓글을 수정하고 수정된 댓글을 반환한다.")
            public void it_return_comments(){
                Comments updateComments = commentService.update(comments.getId(), COMMENT_NEW_TEXT);

                assertThat(updateComments.getContent()).isEqualTo(COMMENT_NEW_TEXT);
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
            public void setUp(){
                for(int i = 0; i < count; i++){
                    commentService.save(getComment(posts.getId()));
                }
            }

            @Test
            @DisplayName("댓글 목록을 리턴한다.")
            void return_comment_list() {
                List<Comments> commentList = commentService.commentList(posts.getId());

                assertThat(commentList.size()).isEqualTo(count);
            }
        }

        @Nested
        @DisplayName("게시물에 댓글이 없다면")
        class Context_when_not_exist_comments {

            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void return_comment_list() {
                List<Comments> commentList = commentService.commentList(posts.getId());

                assertTrue(commentList.isEmpty());
            }
        }

        @Nested
        @DisplayName("게시물이 존재 하지 않는 다면")
        class Context_when_nont_post{
            @Test
            @DisplayName("찾을 수 없는 게시물이라는 예외를 던진다.")
            void it_throw_PostsNotFoundException() {
                assertThatThrownBy(() -> commentService.commentList(-1L))
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