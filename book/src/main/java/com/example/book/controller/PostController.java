package com.example.book.controller;

import com.example.book.domain.Posts;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.service.PostService;
import javafx.geometry.Pos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        System.out.println(postService);
        return postService.getPost(id);
    }

    @PostMapping
    public Posts create(@RequestBody PostsSaveRequestData requestData){
        return postService.save(requestData);
    }
}
