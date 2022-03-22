package com.example.book.controller;

import com.example.book.domain.User;
import com.example.book.dto.UserSaveRequestData;
import com.example.book.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createUser(@RequestBody @Valid UserSaveRequestData saveRequestData){
        return userService.signUp(saveRequestData);
    }
}
