package com.example.book.dto;

import com.example.book.domain.Posts;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostUpdateRequestData {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder
    public PostUpdateRequestData(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
