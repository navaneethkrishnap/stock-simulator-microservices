package com.NKP.order_service.controller;

import com.NKP.order_service.model.Order;
import com.NKP.order_service.model.OrderStatus;
import com.NKP.order_service.model.OrderType;
import com.NKP.order_service.service.OrderDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("orders/{userId}")
public class OrderDetailsController {

    private final OrderDetailsService detailsService;

    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable("userId") long userId,
            @RequestParam(required = false)OrderStatus status,
            @RequestParam(required = false) OrderType type,
            @PageableDefault(size = 10,sort = "orderPlacedTime", direction = Sort.Direction.DESC) Pageable pageable
            ){
        System.err.println("Called");
        Page<Order> orders = detailsService.getOrders(userId,status,type,pageable);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

}
