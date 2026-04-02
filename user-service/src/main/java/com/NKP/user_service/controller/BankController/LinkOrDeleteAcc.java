package com.NKP.user_service.controller.BankController;

import com.NKP.user_service.dto.BankAccountLinkDTO;
import com.NKP.user_service.service.bankService.BankAccountLinkService;
import com.NKP.user_service.service.bankService.DeleteBankAccService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class LinkOrDeleteAcc {

    private final BankAccountLinkService linkService;
    private final DeleteBankAccService deleteBankAccService;

    @PostMapping("/link")
    public ResponseEntity<String> linkBankAccount(@RequestParam long userId, @Valid @RequestBody BankAccountLinkDTO accountLinkDTO){
        boolean status = linkService.linkAccount(userId,accountLinkDTO);

        if(status){
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<>("FAILED", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBankAccount(@RequestParam long userId){

        if( deleteBankAccService.deleteBankAccount(userId)){
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<>("FAILED", HttpStatus.OK);
    }
}
