package com.NKP.user_service.controller.userAccount;

import com.NKP.user_service.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/close-account")
@RequiredArgsConstructor
public class CloseAccount {

    private final UserAccountService userAccountService;

    @DeleteMapping
    public ResponseEntity<String> deleteAccount(@RequestParam long userId){

        userAccountService.deleteAccount(userId);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

}
