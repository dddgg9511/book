package com.example.book.errors;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("해당 이메일 로그인에 실패했습니다. email: " + email);
    }
}
