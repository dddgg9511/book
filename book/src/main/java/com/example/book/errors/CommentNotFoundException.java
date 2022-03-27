package com.example.book.errors;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("해당 댓글이 없습니다. id = " + id);
    }
}
