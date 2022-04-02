package com.example.book.domain;

import com.example.book.dto.PostUpdateRequestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Nested
@DisplayName("게시물")
class PostsTest {
    private static final String TITLE = "게시물 제목";
    private static final String CONTENT = "게시물 내용";
    private static final String AUTHOR = "저자";
    private static final String EMAIL = "test@test.com";
    private static final String NEW_TITLE = "새로운 제목";
    private static final String NEW_CONTENT = "새로운 내용";

    Posts posts;

    @BeforeEach
    public void setUp(){
        posts = Posts.builder()
                .title(TITLE)
                .content(CONTENT)
                .author(AUTHOR)
                .email(EMAIL)
                .build();
    }


    @Nested
    @DisplayName("게시물은 수정은")
    class Descrive_post_update{
        @Nested
        @DisplayName("요청이 들어오면")
        class context_with_requestData{
            PostUpdateRequestData updateRequestData;
            @BeforeEach
            public void setUp(){
                updateRequestData = PostUpdateRequestData.builder()
                        .title(NEW_TITLE)
                        .content(NEW_CONTENT)
                        .build();
            }

            @Test
            @DisplayName("게시물을 수정하고 수정이력을 생성한다,")
            public void it_update_self_and_create_history(){
                posts.update(updateRequestData);
                assertThat(posts.getTitle()).isEqualTo(NEW_TITLE);
                assertThat(posts.getContent()).isEqualTo(NEW_CONTENT);

                List<postHistory> histories = posts.getHistories();
                postHistory history = histories.stream().findFirst().get();
                assertThat(history.getPost()).isEqualTo(posts);
                assertThat(history.getTitle()).isEqualTo(posts.getTitle());
                assertThat(history.getContent()).isEqualTo(NEW_CONTENT);
            }
        }
    }

}