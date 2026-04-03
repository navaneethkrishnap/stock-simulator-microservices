package com.NKP.user_service.controller.order;

import com.NKP.user_service.dto.order.OrderPaymentRequestDTO;
import com.NKP.user_service.service.order.OrderPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/trade")
@RequiredArgsConstructor
public class OrderPaymentController {

    private final OrderPaymentService orderPaymentService;

    @PostMapping("/buy")
    public ResponseEntity<String> deductBuyOrderFunds(@RequestBody OrderPaymentRequestDTO paymentRequestDTO){
        orderPaymentService.deductBalance(paymentRequestDTO);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PostMapping("/refund-payment-buy")
    public ResponseEntity<String> refundBuyOrderPayment(@RequestBody OrderPaymentRequestDTO requestDTO){
        orderPaymentService.refundBalance(requestDTO);
        return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
    }

    @PostMapping("/sell")
    public ResponseEntity<String> receiveSellOrderFunds(@RequestBody OrderPaymentRequestDTO receivePaymenetDTO){
        orderPaymentService.sellOrderFunds(receivePaymenetDTO);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PostMapping("/redo-sell")
    public ResponseEntity<String> redoSellOrderFunds(@RequestBody OrderPaymentRequestDTO requestDTO){
        orderPaymentService.redoSellOrderFunds(requestDTO);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
