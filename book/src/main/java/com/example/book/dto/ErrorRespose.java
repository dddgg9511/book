package com.example.book.dto;

import lombok.Getter;

@Getter
public class ErrorRespose {
    private String message;

    public ErrorRespose(String message){this.message = message;}
}
