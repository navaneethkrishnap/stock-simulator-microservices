package com.NKP.user_service.service.order;

import com.NKP.user_service.dto.order.OrderPaymentRequestDTO;
import com.NKP.user_service.model.FundsTransaction;
import com.NKP.user_service.model.FundsTransactionType;
import com.NKP.user_service.model.User;
import com.NKP.user_service.repo.FundsTransactionRepository;
import com.NKP.user_service.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPaymentService {

    private final UserRepository userRepository;
    private final FundsTransactionRepository fundsTransactionRepository;

    @Transactional
    public void deductBalance(OrderPaymentRequestDTO requestDTO){
        long userId = requestDTO.getUserId();
        Long orderId = requestDTO.getOrderId();
        BigDecimal amount = requestDTO.getAmount();

        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal availAmount = user.getAvailBalance();

        if(availAmount.compareTo(amount) < 0){
            throw new RuntimeException("Insufficient balance. Order cannot be executed.");
        }

        user.setAvailBalance(availAmount.subtract(amount));
        userRepository.save(user);

        FundsTransaction transactionLog = FundsTransaction.builder()
                .orderId(orderId)
                .userId(userId)
                .type(FundsTransactionType.DEDUCT_BUY)
                .amount(amount)
                .reversed(false)
                .createdAt(LocalDateTime.now())
                .build();
        fundsTransactionRepository.save(transactionLog);
    }

    @Transactional
    public void refundBalance(OrderPaymentRequestDTO requestDTO){

        Long orderId = requestDTO.getOrderId();

        Optional<FundsTransaction> transactionLogOpt = fundsTransactionRepository.findByOrderId(orderId);

        if(transactionLogOpt.isEmpty()){
            log.info("No funds transaction found for orderId={}. nothing to refund", orderId);
            return;
        }

        FundsTransaction transactionLog = transactionLogOpt.get();

        if(transactionLog.getType() != FundsTransactionType.DEDUCT_BUY){
            log.warn("Transaction Log entry for orderId={} is not a DEDUCT_BUY transaction (found {}), skipping refund",
                    orderId, transactionLog.getType());
            return;
        }

        if(transactionLog.isReversed()){
            log.info("funds transaction for orderId={} already reversed, skipping", orderId);
            return;
        }


        User user = userRepository.findById(transactionLog.getUserId())
                .orElseThrow(()-> new RuntimeException("User not found"));

        user.setAvailBalance(user.getAvailBalance().add(transactionLog.getAmount()));
        userRepository.save(user);

        transactionLog.setReversed(true);
        fundsTransactionRepository.save(transactionLog);
    }

    @Transactional
    public void sellOrderFunds(OrderPaymentRequestDTO receivePaymentDTO) {
        long userId = receivePaymentDTO.getUserId();
        Long orderId = receivePaymentDTO.getOrderId();
        BigDecimal amount = receivePaymentDTO.getAmount();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        user.setAvailBalance(user.getAvailBalance().add(amount));
        userRepository.save(user);

        FundsTransaction transactionLog = FundsTransaction.builder()
                .orderId(orderId)
                .userId(userId)
                .type(FundsTransactionType.RECEIVE_SELL)
                .amount(amount)
                .reversed(false)
                .createdAt(LocalDateTime.now())
                .build();

        fundsTransactionRepository.save(transactionLog);
    }

    @Transactional
    public void redoSellOrderFunds(OrderPaymentRequestDTO dto){
        Long orderId = dto.getOrderId();

        Optional<FundsTransaction> transactionLogOpt = fundsTransactionRepository.findByOrderId(orderId);

        if(transactionLogOpt.isEmpty()){
            log.info("No funds transaction found for orderId={}, nothing to reverse(sell)", orderId);
            return;
        }

        FundsTransaction transactionLog = transactionLogOpt.get();

        if(transactionLog.getType() != FundsTransactionType.RECEIVE_SELL){
            log.warn("Transaction log entry for orderId={} is not a RECEIVE_SELL transaction (found {})," +
                    "skipping reversal", orderId, transactionLog.getType());
            return;
        }

        if(transactionLog.isReversed()){
            log.info("Funds transaction for orderId={} already reversed, skipping", orderId);
            return;
        }

        User user = userRepository.findById(transactionLog.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal newBalance = user.getAvailBalance().subtract(transactionLog.getAmount());

        if(newBalance.compareTo(BigDecimal.ZERO) < 0){
            log.error("Cannot fully reverse funds for orderId={}: would drive balance negative " +
                    "current={}, reversal amount={}", orderId,user.getAvailBalance(), transactionLog.getAmount());
            throw new IllegalStateException("Insufficient balance to reverse sell order funds for orderId="+ orderId);
        }
        user.setAvailBalance(newBalance);
        userRepository.save(user);

        transactionLog.setReversed(true);
        fundsTransactionRepository.save(transactionLog);
    }
}
