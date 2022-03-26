package com.example.book.controller;

import com.example.book.domain.Comments;
import com.example.book.dto.CommentsSaveData;
import com.example.book.errors.InvalidParameterException;
import com.example.book.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comments create(@RequestBody @Valid CommentsSaveData commentsSaveData, BindingResult result){
        if(result.hasErrors()){
            throw new InvalidParameterException(result);
        }
        return commentService.save(commentsSaveData);
    }

    @GetMapping("/{postId}")
    public List<Comments> list(@PathVariable Long postId) {
        return commentService.commentList(postId);
    }
}
