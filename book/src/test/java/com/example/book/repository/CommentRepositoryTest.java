package com.example.book.repository;

import com.example.book.domain.Comments;
import com.example.book.domain.Posts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.xml.stream.events.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    private static final String TITLE = "테스트 제목";
    private static final String CONTENT = "테스트 내용";
    private static final String AUTHOR = "책 저자";
    private static final String EMAIL = "test@gmail.com";

    private static final String COMMENT_CONTENT = "댓글 내용";

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Nested
    @DisplayName("댓글 목록 조회")
    class Describe_comment_list {
        Posts post;

        @BeforeEach
        void setUp() {
            post = postRepository.save(Posts.builder()
                    .title(TITLE)
                    .content(CONTENT)
                    .author(AUTHOR)
                    .email(EMAIL)
                    .build());
        }

        @Nested
        @DisplayName("게시물에 댓글이 존재하면")
        class Context_when_exist_comments {
            final int count = 10;

            @BeforeEach
            public void setUp(){
                for(int i = 0; i < count; i++){
                    commentRepository.save(Comments.builder()
                                    .postId(post.getId())
                                    .content(COMMENT_CONTENT)
                                    .email(EMAIL).build());
                }
            }

            @Test
            @DisplayName("댓글 목록을 리턴한다.")
            void return_comment_list() {
                List<Comments> commentsList = commentRepository.findByPostId(post.getId());

                assertThat(commentsList.size()).isEqualTo(count);
            }
        }

        @Nested
        @DisplayName("게시물에 댓글이 없다면")
        class Context_when_not_exist_comments {

            @Test
            @DisplayName("빈 리스트를 리턴한다.")
            void return_comment_list() {
                List<Comments> commentsList = commentRepository.findByPostId(post.getId());

                assertTrue(commentsList.isEmpty());
            }
        }
    }
}