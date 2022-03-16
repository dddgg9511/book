package com.example.book.controller;

import com.example.book.domain.User;
import com.example.book.dto.UserSaveRequestData;
import com.example.book.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    public User createUser(UserSaveRequestData saveRequestData){
        return userService.signUp(saveRequestData);
    }
}
