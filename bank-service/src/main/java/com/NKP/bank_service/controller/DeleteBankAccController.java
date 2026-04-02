package com.NKP.bank_service.controller;

import com.NKP.bank_service.service.AccountDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delete")
@RequiredArgsConstructor
public class DeleteBankAccController {

    private final AccountDeleteService accountDeleteService;

    @DeleteMapping("/bank-account")
    public ResponseEntity<String> deleteBankAccount(@RequestParam long userId){
        if(accountDeleteService.deleteAccount(userId)){
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<>("FAILED", HttpStatus.OK);
    }
}
