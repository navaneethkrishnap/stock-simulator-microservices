package com.NKP.order_service.controller;

import com.NKP.order_service.dto.OrderRequestDTO;
import com.NKP.order_service.service.PlaceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders/place")
@RequiredArgsConstructor
public class OrderController {

    private final PlaceOrderService placeOrderService;

    @PostMapping("/buy")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDTO orderRequest){
        placeOrderService.placeOrder(orderRequest);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
