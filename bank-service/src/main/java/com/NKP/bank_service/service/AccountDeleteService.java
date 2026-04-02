package com.NKP.bank_service.service;

import com.NKP.bank_service.model.BankDetails;
import com.NKP.bank_service.repo.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountDeleteService {
    private final BankRepository bankRepository;

    public boolean deleteAccount(long userId){
        long id;
        Optional<BankDetails> bankDetails = bankRepository.findByUserId(userId);

        if(bankDetails.isPresent()){
            id = bankDetails.get().getId();
            bankRepository.deleteById(id);
            return true;
        }
        throw new RuntimeException("Bank account doesn't exists");
    }
}
