package com.NKP.user_service.controller.BankController;

import com.NKP.user_service.dto.funds.AddMoneyRequestDTO;
import com.NKP.user_service.dto.funds.WithdrawMoneyRequestDTO;
import com.NKP.user_service.model.WithdrawalStatus;
import com.NKP.user_service.service.bankService.FundTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/funds")
@RequiredArgsConstructor
public class FundTransfer {

    private final FundTransferService fundTransferService;

    // deposit funds to account
    @PostMapping("/add")
    public ResponseEntity<String> requestFundToBank(@RequestBody AddMoneyRequestDTO moneyRequestDTO){

        fundTransferService.requestFunds(moneyRequestDTO); // call this from service class
        return new ResponseEntity<>("Funds added successfully", HttpStatus.OK);
    }

    // withdraw funds from balance to bank account
    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawalStatus> withdrawBalance(@RequestBody WithdrawMoneyRequestDTO moneyRequestDTO){

        WithdrawalStatus status = fundTransferService.processWithdrawal(moneyRequestDTO);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }


}
