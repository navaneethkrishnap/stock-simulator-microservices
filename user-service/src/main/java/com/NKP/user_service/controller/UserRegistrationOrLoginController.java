package com.NKP.user_service.controller;

import com.NKP.user_service.dto.UserLoginDTO;
import com.NKP.user_service.dto.UserRegistrationDTO;
import com.NKP.user_service.service.RegistrationOrLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserRegistrationOrLoginController {

    private final RegistrationOrLoginService registrationOrLoginService;

    @PostMapping("/register")
    public ResponseEntity<String> userRegistration(@RequestBody UserRegistrationDTO registrationDTO){
           registrationOrLoginService.createAccount(registrationDTO);
           return new ResponseEntity<>("Account created",HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody UserLoginDTO loginDTO){
        boolean status = registrationOrLoginService.loginService(loginDTO);
        if(status){
            return new ResponseEntity<>("Login Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Please enter correct details", HttpStatus.UNAUTHORIZED);
    }



}
