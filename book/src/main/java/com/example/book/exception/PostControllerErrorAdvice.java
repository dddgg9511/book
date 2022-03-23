package com.example.book.exception;

import com.example.book.dto.ErrorRespose;
import com.example.book.errors.PostsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class PostControllerErrorAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostsNotFoundException.class)
    public ErrorRespose handleCommentNotFound() {
        return new ErrorRespose("게시물을 찾을 수 없습니다.");
    }
}
