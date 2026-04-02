package com.NKP.user_service.openfeign;

import com.NKP.user_service.dto.BankAccountLinkDTO;
import com.NKP.user_service.dto.funds.AddMoneyRequestDTO;
import com.NKP.user_service.dto.funds.WithdrawMoneyRequestDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("BANK-SERVICE")
public interface BankClient {

    @PostMapping("link/account")
    ResponseEntity<String> linkBankAccount(
            @RequestParam long userId,
            @Valid @RequestBody BankAccountLinkDTO linkDTO);

    @PostMapping("bank/request-funds")
    ResponseEntity<String> requestFundToBank(@RequestBody AddMoneyRequestDTO moneyRequestDTO);

    @PostMapping("bank/receive-funds")
    ResponseEntity<String> addFundsToBankAccount(@RequestBody WithdrawMoneyRequestDTO withdrawMoneyRequestDTO);

    @DeleteMapping("delete/bank-account")
    ResponseEntity<String> deleteBankAccount(@RequestParam long userId);
}
