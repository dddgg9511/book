package com.example.book.errors;

public class LoginNotMatchPasswordException extends RuntimeException {
    public LoginNotMatchPasswordException(String email) {
        super("비밀번호가 일치 하지 않습니다. password: " + email);
    }
}
