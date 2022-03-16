package com.example.book.security;

import com.example.book.domain.UserRole;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class UserAuthentication extends AbstractAuthenticationToken {
    @Getter
    private String accessToken;

    private Long userId;

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    public UserAuthentication(UserRole role, String accessToken, Long userId) {
        super(authorities(role));
        this.accessToken = accessToken;
        this.userId = userId;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    private static List<GrantedAuthority> authorities(UserRole role) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }
}
