package com.example.book.repository;

import com.example.book.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    public List<Comments> findByPostId(Long postId);
}
