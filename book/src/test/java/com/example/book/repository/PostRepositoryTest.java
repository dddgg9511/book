package com.example.book.repository;

import com.example.book.domain.Posts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {
    private static final String TITLE = "테스트 게시글";
    private static final String CONTENT = "테스트 본문";
    private static final String AUTHOR = "테스트 저자";
    private static final String EMAIL = "test@gmail.com";

    @Autowired
    private PostRepository postRepository;

    @Nested
    @DisplayName("게시글 저장하고 불러오기는")
    class Describe_save_posts {
        @Nested
        @DisplayName("게시글 정보를 받아 저장하고")
        class Context_has_post_info {
            Posts posts;

            @BeforeEach
            void setUp() {
                posts = postRepository.save(Posts.builder()
                        .title(TITLE)
                        .content(CONTENT)
                        .author(AUTHOR)
                        .email(EMAIL)
                        .build());
            }

            @Test
            @DisplayName("저장된 게시글 목록을 리턴한다.")
            void it_return_list() {
                List<Posts> postsList = postRepository.findAll();

                Posts posts = postsList.get(0);
                assertThat(posts.getTitle()).isEqualTo(TITLE);
                assertThat(posts.getContent()).isEqualTo(CONTENT);
            }
        }
    }
}