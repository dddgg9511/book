package com.example.book.errors;

public class UserEmailDuplicationException extends RuntimeException{
    public UserEmailDuplicationException(String email){
        super("이미 존재하는 이메일 입니다. email= " + email);
    }
}
