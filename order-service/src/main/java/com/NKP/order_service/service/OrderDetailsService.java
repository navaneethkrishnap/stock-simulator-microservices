package com.NKP.order_service.service;

import com.NKP.order_service.model.Order;
import com.NKP.order_service.model.OrderStatus;
import com.NKP.order_service.model.OrderType;
import com.NKP.order_service.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderRepository orderRepository;

    public Page<Order> getOrders(long userId, OrderStatus status, OrderType type, Pageable pageable){

        // check type string checking in api testing

        if(status != null && type != null){
            return orderRepository.findByUserIdAndStatusAndType(userId, status, type, pageable);
        }

        if(status != null){
            return orderRepository.findByUserIdAndStatus(userId, status, pageable);
        }

        if(type != null){
            return orderRepository.findByUserIdAndType(userId, type,pageable);
        }

        return orderRepository.findAllByUserId(userId,pageable);
    }
}
