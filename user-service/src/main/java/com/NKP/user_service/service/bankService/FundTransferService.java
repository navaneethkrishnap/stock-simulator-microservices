package com.NKP.user_service.service.bankService;

import com.NKP.user_service.dto.funds.AddMoneyRequestDTO;
import com.NKP.user_service.dto.funds.WithdrawMoneyRequestDTO;
import com.NKP.user_service.model.User;
import com.NKP.user_service.model.WithdrawalStatus;
import com.NKP.user_service.openfeign.BankClient;
import com.NKP.user_service.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FundTransferService {

    private final BankClient bankClient;
    private final UserRepository userRepository;

    @Autowired
    private Environment env;

    @PostConstruct
    public void checkDB(){
        System.out.println("DB url in use "+ env.getProperty("spring.datasource.url"));
    }

    public void requestFunds(AddMoneyRequestDTO moneyRequestDTO) {

        long userId = moneyRequestDTO.getUserId();
        BigDecimal requestedFunds = moneyRequestDTO.getAmount();


        if(requestedFunds.compareTo(new BigDecimal("100")) < 0)
            throw new IllegalArgumentException("Funds must be at least 100");

        String result = bankClient.requestFundToBank(moneyRequestDTO).getBody();

        if(!"SUCCESS".equals(result)) {
            throw new RuntimeException("Request Error: Bank transaction failed.");
        }

        Optional<User> userAccount = userRepository.
                findById(userId);

        if(userAccount.isEmpty()){
            throw new RuntimeException("User Account not found");
        }

        User updateUserBal = userAccount.get();
        updateUserBal.setAvailBalance(updateUserBal.getAvailBalance().add(requestedFunds));

        userRepository.save(updateUserBal);

    }

    /**
     * process the withdrawal request coming from user
     * validates and subtracts the funds
     * @param dto
     * @return WithdrawalStatus
     */
    public WithdrawalStatus processWithdrawal(WithdrawMoneyRequestDTO dto){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal availBalance = user.getAvailBalance();
        BigDecimal withdrawRequest = dto.getAmount();

        // validate negative values
        if(withdrawRequest.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Invalid withdrawal amount");
        }

        int res = withdrawRequest.compareTo(availBalance);

        if(res > 0){
            return WithdrawalStatus.INSUFFICIENT_FUNDS;
        }

        String bankReq = bankClient.addFundsToBankAccount(dto).getBody();

        if(!"SUCCESS".equals(bankReq)){
            throw new RuntimeException("Withdraw Error: Bank Transaction failed");
        }

        updateBalance(user,withdrawRequest);

        if(res == 0){
            return WithdrawalStatus.WITHDRAWN_ALL;
        }

        return WithdrawalStatus.SUCCESS;

    }

    @Transactional
    public void updateBalance(User user, BigDecimal amount){
        user.setAvailBalance(user.getAvailBalance().subtract(amount));
        userRepository.save(user);
    }
}
