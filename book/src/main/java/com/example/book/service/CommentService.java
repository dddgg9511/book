package com.example.book.service;

import com.example.book.domain.Comments;
import com.example.book.dto.CommentsSaveData;
import com.example.book.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostService postService;

    public Comments save(CommentsSaveData commentsSaveData) {
        postService.getPost(commentsSaveData.getPostId());

        return commentRepository.save(commentsSaveData.toEntity());
    }
}
