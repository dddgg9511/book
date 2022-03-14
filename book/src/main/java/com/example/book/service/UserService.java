package com.example.book.service;

import com.example.book.domain.User;
import com.example.book.dto.UserSaveRequestData;
import com.example.book.errors.UserEmailDuplicationException;
import com.example.book.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(UserSaveRequestData saveRequestData){
        String email = saveRequestData.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new UserEmailDuplicationException(email);
        }

        User user = saveRequestData.toEntity();

        user.encodePassword(saveRequestData.getPassword(), passwordEncoder);

        return userRepository.save(user);
    }
}
