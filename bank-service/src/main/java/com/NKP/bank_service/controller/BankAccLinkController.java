package com.NKP.bank_service.controller;

import com.NKP.bank_service.dto.BankAccountLinkDTO;
import com.NKP.bank_service.service.AccountLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/link")
@RequiredArgsConstructor
public class BankAccLinkController {

    private final AccountLinkService linkService;

    // implement security and Jwts for getting userId later
    @PostMapping("/account")
    public ResponseEntity<String> linkBankAccount(@RequestParam long userId, @Valid @RequestBody BankAccountLinkDTO linkDTO) {
       linkService.linkAccount(userId,linkDTO);
       return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
