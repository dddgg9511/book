package com.example.book.security;

import com.example.book.domain.UserRole;
import com.example.book.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final AuthenticationService authenticationService;

    public JwtAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication anonymousAuthentication = (UserAuthentication) authentication;
        String accessToken = anonymousAuthentication.getAccessToken();

        Long userId = authenticationService.parseToken(accessToken);

        return new UserAuthentication(UserRole.USER, accessToken, userId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserAuthentication.class);
    }
}

