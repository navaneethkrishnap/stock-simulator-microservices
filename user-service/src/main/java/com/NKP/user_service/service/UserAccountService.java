package com.NKP.user_service.service;

import com.NKP.user_service.model.User;
import com.NKP.user_service.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserRepository userRepository;

    public void deleteAccount(long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User account not found")
        );

        userRepository.delete(user);
    }
}
