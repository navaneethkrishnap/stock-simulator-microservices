package com.NKP.order_service.feign;

import com.NKP.order_service.dto.OrderPaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "USER-CLIENT", url = "http://localhost:8081")
public interface UserClient {

    @PostMapping("trade/buy")
    ResponseEntity<String> deductBuyOrderFunds(@RequestBody OrderPaymentRequestDTO paymentRequestDTO);

    @PostMapping("trade/refund-payment-buy")
    ResponseEntity<String> refundBuyOrderPayment(@RequestBody OrderPaymentRequestDTO requestDTO);

    @PostMapping("trade/sell")
    ResponseEntity<String> receiveSellOrderFunds(@RequestBody OrderPaymentRequestDTO receivePaymenetDTO);

    @PostMapping("trade/redo-sell")
    ResponseEntity<String> redoSellOrderFunds(@RequestBody OrderPaymentRequestDTO requestDTO);

}
