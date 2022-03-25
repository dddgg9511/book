package com.example.book.controller;

import com.example.book.domain.Posts;
import com.example.book.dto.PostUpdateRequestDto;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.errors.InvalidParameterException;
import com.example.book.service.PostService;
import javafx.geometry.Pos;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PostUpdate;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @GetMapping
    public List<Posts> list(){
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public Posts detail(@PathVariable long id){
        return postService.getPost(id);
    }

    @PostMapping
    public Posts create(@RequestBody @Valid PostsSaveRequestData requestData, BindingResult result){
        if(result.hasErrors()){
            throw new InvalidParameterException(result);
        }
        return postService.save(requestData);
    }

    @PatchMapping("/{id}")
    public Posts update(@PathVariable long id, @RequestBody @Valid PostUpdateRequestDto updateDto,
                        BindingResult result){
        if(result.hasErrors()){
            throw new InvalidParameterException(result);
        }
        return postService.update(id, updateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
