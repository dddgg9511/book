package com.example.book.service;

import com.example.book.domain.Comments;
import com.example.book.dto.CommentsSaveData;
import com.example.book.errors.CommentNotFoundException;
import com.example.book.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostService postService;

    public Comments save(CommentsSaveData commentsSaveData) {
        postService.getPost(commentsSaveData.getPostId());

        return commentRepository.save(commentsSaveData.toEntity());
    }

    public List<Comments> commentList(Long postId) {
        postService.getPost(postId);

        return commentRepository.findByPostId(postId);
    }

    public Comments update(Long id, String content){
        Comments comments = getComments(id);

        comments.setContent(content);

        return comments;
    }

    public Comments getComments(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }
}
