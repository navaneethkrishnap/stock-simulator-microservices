package com.NKP.user_service.service.bankService;

import com.NKP.user_service.dto.BankAccountLinkDTO;
import com.NKP.user_service.openfeign.BankClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BankAccountLinkService {

    private final BankClient bankClient;

    public boolean linkAccount(long userId, BankAccountLinkDTO accountLinkDTO){

        String status = bankClient.linkBankAccount(userId, accountLinkDTO).getBody();

        return Objects.equals(status, "SUCCESS");
    }
}
