package com.example.book.service;

import com.example.book.domain.Posts;
import com.example.book.dto.PostUpdateRequestDto;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.errors.PostsNotFoundException;
import com.example.book.repository.PostRepository;
import lombok.Builder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("게시물 관리")
class PostServiceTest {
    private static final String TITLE = "테스트 제목";
    private static final String CONTENT = "테스트 내용";
    private static final String AUTHOR = "책 저자";
    private static final String EMAIL = "test@gmail.com";
    private static final String NEW_TITLE = "새로운 제목";
    private static final String NEW_CONTENT = "새로운 내용";
    private static final Long ID = 1L;

    @Autowired
    private PostService postService;


    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void cleanUp() {
        postRepository.deleteAll();
    }

    @Nested
    @DisplayName("게시물 저장은")
    class Discribe_save {
        Posts post;

        @Nested
        @DisplayName("게시물을 입력받으면")
        class Context_when_post {

            @Test
            @DisplayName("저장하고 게시물을 리턴한다.")
            void it_return_post() {
                post = postService.save(createPost("" + 1));

                assertThat(post.getTitle()).isEqualTo(TITLE + 1);
                assertThat(post.getContent()).isEqualTo(CONTENT);
                assertThat(post.getAuthor()).isEqualTo(AUTHOR);
            }
        }
    }

    @Nested
    @DisplayName("게시물 목록 조회는")
    class Discribe_getPosts {
        @Nested
        @DisplayName("게시물이 존재한다면")
        class Context_exist_posts {
            final int postCount = 10;

            @BeforeEach
            void setUp() {
                for (int i = 0; i < postCount; i++) {
                    postService.save(createPost("" + i));
                }
            }

            @DisplayName("게시물 전체 목록을 반환한다.")
            @Test
            void it_return_posts() {
                assertThat(postService.getPosts()).isNotEmpty();
                assertThat(postService.getPosts()).hasSize(postCount);
            }
        }

        @Nested
        @DisplayName("게시물이 존재하지 않는다면")
        class Context_none_posts {
            @BeforeEach
            void setUp() {
                postRepository.deleteAll();
            }

            @DisplayName("빈 리스트를 리턴한다.")
            @Test
            void it_return_empty_list() {
                assertThat(postService.getPosts()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("게시물 목록 수정은")
    class Discribe_update{
        PostUpdateRequestDto postUpdateRequestDto;

        @BeforeEach
        public void setUp(){
            postUpdateRequestDto = PostUpdateRequestDto.builder()
                    .title(NEW_TITLE)
                    .content(NEW_CONTENT)
                    .build();
        }

        @Nested
        @DisplayName("게시물이 존재한다면")
        class Context_exist_posts{
            Posts post;

            @BeforeEach
            public void setUp(){
                post = postService.save(createPost(""));
            }

            @DisplayName("게시물을 수정하고 수정된 게시물을 반환한다.")
            @Test
            public void it_return_posts(){
                Posts updatePost = postService.update(post.getId(), postUpdateRequestDto);

                assertThat(updatePost.getTitle()).isEqualTo(NEW_TITLE);
                assertThat(updatePost.getContent()).isEqualTo(NEW_CONTENT);
            }
        }
        
        @Nested
        @DisplayName("게시물이 존재하지 않는다면")
        class Context_none_posts{
            @Test
            @DisplayName("게시물을 찾을수 없다는 에러를 던진다.")
            public void it_throw_postsNoFoundException() throws Exception{
                assertThatThrownBy(() -> postService.update(1, postUpdateRequestDto))
                        .isInstanceOf(PostsNotFoundException.class);
            }
        }
    }





    private PostsSaveRequestData createPost(String suffix){
        return PostsSaveRequestData.builder()
                .title(TITLE + suffix)
                .content(CONTENT)
                .author(AUTHOR)
                .email(EMAIL)
                .build();
    }
}