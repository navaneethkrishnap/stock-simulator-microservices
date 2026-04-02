package com.NKP.user_service.service.order;

import com.NKP.user_service.dto.order.OrderPaymentRequestDTO;
import com.NKP.user_service.model.User;
import com.NKP.user_service.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderPaymentService {

    private final UserRepository userRepository;

    @Transactional
    public void deductBalance(OrderPaymentRequestDTO requestDTO){
        long userId = requestDTO.getUserId();
        BigDecimal amount = requestDTO.getAmount();

        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal availAmount = user.getAvailBalance();

        int res = availAmount.compareTo(amount);

        if(res < 0){
            throw new RuntimeException("Insufficient balance. Order cannot be executed.");
        }

        user.setAvailBalance(availAmount.subtract(amount));
        userRepository.save(user);
    }

    @Transactional
    public void refundBalance(OrderPaymentRequestDTO requestDTO){

        long userId = requestDTO.getUserId();
        BigDecimal refundAmount = requestDTO.getAmount();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        user.setAvailBalance(user.getAvailBalance().add(refundAmount));
        userRepository.save(user);
    }

}
