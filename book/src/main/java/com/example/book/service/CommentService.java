package com.example.book.service;

import com.example.book.domain.Comments;
import com.example.book.dto.CommentsSaveData;
import com.example.book.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comments save(CommentsSaveData commentsSaveData) {
        return null;
    }
}
