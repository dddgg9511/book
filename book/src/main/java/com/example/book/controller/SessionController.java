package com.example.book.controller;

import com.example.book.dto.SessionResponseData;
import com.example.book.dto.UserLoginData;
import com.example.book.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/p")
@RestController
@RequiredArgsConstructor
public class SessionController {
    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(@RequestBody @Valid UserLoginData userLoginData){
        String accessToken = authenticationService.login(userLoginData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
