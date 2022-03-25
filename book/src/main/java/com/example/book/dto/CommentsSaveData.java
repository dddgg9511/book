package com.example.book.dto;

import com.example.book.domain.Comments;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CommentsSaveData {
    /**
     * 댓글을 등록할 게시물 아이디
     */
    @NotNull(message = "게시물 아이디는 필수입니다.")
    private Long postId;
    /**
     * 댓글 내용
     */
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
    /**
     * 작성자 이메일
     */
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @Builder
    public CommentsSaveData(Long postId, String content, String email) {
        this.postId = postId;
        this.content = content;
        this.email = email;
    }

    public Comments toEntity() {
        return Comments.builder()
                .email(email)
                .postId(postId)
                .content(content)
                .build();
    }
}
