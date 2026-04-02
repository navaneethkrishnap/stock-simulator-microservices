package com.NKP.user_service.service;

import com.NKP.user_service.dto.UserLoginDTO;
import com.NKP.user_service.dto.UserRegistrationDTO;
import com.NKP.user_service.model.User;
import com.NKP.user_service.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RegistrationOrLoginService {

    private final UserRepository userRepository;

    @Transactional
    public void createAccount(UserRegistrationDTO registerDTO){

        if(userRepository.findByPhoneNo(registerDTO.getPhoneNo()).isPresent()){
            throw new RuntimeException("Account already exists. You cannot create multiple account");
        }

        User user = new User();
        user.setFullName(registerDTO.getFullName());
        user.setDob(registerDTO.getDob());
        user.setPassword(registerDTO.getPassword());
        user.setEmailId(registerDTO.getEmailId());
        user.setAvailBalance(BigDecimal.ZERO);
        user.setPhoneNo(registerDTO.getPhoneNo());

        // logging
//         System.err.println(registerDTO.getPassword()+" "+ registerDTO.getDob()+ " "+ registerDTO.getEmailId()
//                 +" "+ registerDTO.getFullName());

        userRepository.save(user);
    }

    public boolean loginService(UserLoginDTO loginDTO) {

        User user = userRepository.findByEmailId(loginDTO.getEmailId())
                .orElseThrow(() -> new IllegalArgumentException("Email doesn't exists"));

        return loginDTO.getPassword().equals(user.getPassword());
    }
}
