package com.example.book.controller;

import com.example.book.domain.Posts;
import com.example.book.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PostController {
    private PostService postService;

    @GetMapping
    public List<Posts> list(){
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public Posts detail(@PathVariable long id){
        return postService.getPost(id);
    }
}
