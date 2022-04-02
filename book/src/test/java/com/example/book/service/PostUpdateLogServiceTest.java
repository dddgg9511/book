package com.example.book.service;

import com.example.book.domain.PostUpdateLog;
import com.example.book.domain.Posts;
import com.example.book.dto.PostUpdateRequestData;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.errors.PostsNotFoundException;
import com.example.book.repository.PostUpdateLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("게시물 수정 로그 관리")
class PostUpdateLogServiceTest {
    private static final String TITLE = "테스트 제목";
    private static final String CONTENT = "테스트 내용";
    private static final String AUTHOR = "책 저자";
    private static final String EMAIL = "test@gmail.com";
    private static final String NEW_TITLE = "새로운 제목";
    private static final String NEW_CONTENT = "새로운 내용";

    @Autowired
    PostUpdateLogService postUpdateLogService;

    @Autowired
    PostService postService;

    @Autowired
    PostUpdateLogRepository postUpdateLogRepository;

    Posts posts;

    @BeforeEach
    public void setUp(){
        posts = postService.save(createPost());
    }

    @Nested
    @DisplayName("로그 생성은")
    class Describe_save_postUpdateLog{
        PostUpdateRequestData updateData;
        @BeforeEach
        public void setUp(){
            updateData = updatePost("");
        }

        @Nested
        @DisplayName("존재하는 게시물이면")
        class context_with_exist_posts{
            @Test
            @DisplayName("로그를 생성하고 생성된 로그를 반환한다.")
            public void it_return_postUpdateLog(){
                PostUpdateLog log = postUpdateLogService.save(posts.getId(), updateData);
                assertThat(log.getContent()).isEqualTo(updateData.getContent());
                assertThat(log.getTitle()).isEqualTo(updateData.getTitle());
                assertThat(log.getPostId()).isEqualTo(posts.getId());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 게시물이면")
        class context_with_invalid_posts{
            @Test
            @DisplayName("게시물을 찾을 수 없다는 예외를 던진다.")
            public void it_throw_postNotFoundException() throws Exception{
                assertThatThrownBy(() -> postUpdateLogService.save(-1L, updateData))
                        .isInstanceOf(PostsNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("로그 조히는")
    class Descrive_get_postUpdateLogs{
        @Nested
        @DisplayName("존재하는 게시물이면")
        class context_with_exist_posts{
            int size = 30;
            @BeforeEach
            public void setUp(){
                for(int i = 0; i < size; i++){
                    PostUpdateRequestData data = updatePost(i + "");
                    postUpdateLogRepository.save(PostUpdateLog.of(posts.getId(), data));
                }
            }
            @Test
            @DisplayName("게시물의 수정 이력 목록을 반환한다.")
            public void it_return_postUpdateLog_array(){
                List<PostUpdateLog> logList = postUpdateLogService.getPostUpdateLogs(posts.getId());
                assertThat(logList.size()).isEqualTo(size);
            }
        }

        @Nested
        @DisplayName("게시물이 존재하지 않는다면")
        class context_with_invalid_posts{
            @Test
            @DisplayName("게시물을 찾을 수 없다는 예외를 던진다.")
            public void it_throw_postNotFoundException() throws Exception{
                assertThatThrownBy(() -> postUpdateLogService.getPostUpdateLogs(posts.getId()))
                        .isInstanceOf(PostsNotFoundException.class);
            }
        }
    }

    private PostsSaveRequestData createPost(){
        return PostsSaveRequestData.builder()
                .title(TITLE)
                .content(CONTENT)
                .author(AUTHOR)
                .email(EMAIL)
                .build();
    }

    public PostUpdateRequestData updatePost(String suffix){
        return PostUpdateRequestData.builder()
                .title(NEW_TITLE + suffix)
                .content(NEW_CONTENT + suffix)
                .build();
    }
}