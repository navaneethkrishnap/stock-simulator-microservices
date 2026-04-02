package com.NKP.bank_service.controller.funds;

import com.NKP.bank_service.dto.AddMoneyRequestDTO;
import com.NKP.bank_service.dto.WithdrawMoneyRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank")
public class FundController {

    @PostMapping("/request-funds")
    public ResponseEntity<String> requestFundToBank(@RequestBody AddMoneyRequestDTO moneyRequestDTO){

        // check userId verify bank Id
        // payment logic
        // if success withdrew from bank account
        // else return insufficient or network error

        return ResponseEntity.ok("SUCCESS");

        // add FAILURE
    }

    @PostMapping("/receive-funds")
    public ResponseEntity<String> addFundsToBankAccount(@RequestBody WithdrawMoneyRequestDTO withdrawMoneyRequestDTO){

        // check userId
        // verify bank
        // call service that implements payments
        // if success add to bank account
        // else return error

        return ResponseEntity.ok("SUCCESS");
    }
}
