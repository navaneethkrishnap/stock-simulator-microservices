package com.NKP.user_service.controller.userAccount;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
public class AccountUpdateSettings {

    @PostMapping("/update-email")
    public ResponseEntity<String> updateEmailId(){
        return null;
    }

    @PostMapping("/update-phoneNo")
    public ResponseEntity<String> updatePhoneNo(){
        return null;
    }

}
