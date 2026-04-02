package com.NKP.user_service.service.bankService;

import com.NKP.user_service.openfeign.BankClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBankAccService {
    private final BankClient bankClient;


    public boolean deleteBankAccount(long userId) {
        String status = bankClient.deleteBankAccount(userId).getBody();
        return "SUCCESS".equals(status);
    }
}
