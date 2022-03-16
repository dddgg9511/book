package com.example.book.service;

import com.example.book.domain.User;
import com.example.book.dto.UserLoginData;
import com.example.book.errors.LoginFailException;
import com.example.book.errors.LoginNotMatchPasswordException;
import com.example.book.repository.UserRepository;
import com.example.book.util.WebTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final WebTokenUtil webTokenUtil;
    private final UserRepository userRepository;

    public AuthenticationService(WebTokenUtil webTokenUtil, UserRepository userRepository) {
        this.webTokenUtil = webTokenUtil;
        this.userRepository = userRepository;
    }

    public String login(UserLoginData userLoginData){
        String userEmail = userLoginData.getEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new LoginFailException(userEmail));
        if(!user.authenticate(userLoginData.getPassword())){
            throw new LoginNotMatchPasswordException(user.getEmail());
        }
        return webTokenUtil.encode(user.getId());
    }

    public Long parseToken(String accessToken) {
        Claims claims = webTokenUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
