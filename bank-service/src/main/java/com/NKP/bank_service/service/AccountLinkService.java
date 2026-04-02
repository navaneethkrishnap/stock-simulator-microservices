package com.NKP.bank_service.service;

import com.NKP.bank_service.dto.BankAccountLinkDTO;
import com.NKP.bank_service.model.BankDetails;
import com.NKP.bank_service.repo.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountLinkService {

    private final BankRepository bankRepository;

    public void linkAccount(long userId,BankAccountLinkDTO accountLinkDTO){
        
        Optional<BankDetails> existingBankAcc = bankRepository.findByUserId(userId);

        if(existingBankAcc.isPresent()){
            throw new IllegalStateException("Bank Account already exists");
        }

        BankDetails bankDetails = addBankDetails(userId, accountLinkDTO);

        bankRepository.save(bankDetails);
    }

    private static BankDetails addBankDetails(long userId, BankAccountLinkDTO accountLinkDTO) {
        BankDetails bankDetails = new BankDetails();

        bankDetails.setUserId(userId);

        bankDetails.setAccountName(accountLinkDTO.getAccountName());
        bankDetails.setAccountNumber(accountLinkDTO.getAccountNumber());
        bankDetails.setBankName(accountLinkDTO.getBankName());
        bankDetails.setBranchName(accountLinkDTO.getBranchName());
        bankDetails.setIfsc(accountLinkDTO.getIfsc());
        bankDetails.setAadhaarNumber(accountLinkDTO.getAadhaarNumber());
        bankDetails.setPanNumber(accountLinkDTO.getPanNumber());
        return bankDetails;
    }
}
