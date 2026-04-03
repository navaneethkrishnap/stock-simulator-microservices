package com.NKP.order_service.controller;

import com.NKP.order_service.dto.OrderRequestDTO;
import com.NKP.order_service.dto.OrderResponseDTO;
import com.NKP.order_service.service.PlaceOrderService;
import jakarta.validation.Valid;
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
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequest){
        OrderResponseDTO response = placeOrderService.placeOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellOrder(@Valid @RequestBody OrderRequestDTO orderRequest){
        placeOrderService.sellOrder(orderRequest);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
