package com.NKP.order_service.repo;

import com.NKP.order_service.model.Order;
import com.NKP.order_service.model.OrderStatus;
import com.NKP.order_service.model.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByUserIdAndStatus(long userId, OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByUserIdAndType(long userId, OrderType orderType, Pageable pageable);

    Page<Order> findByUserIdAndStatusAndType(long userId, OrderStatus status, OrderType type, Pageable pageable);

    Page<Order> findAllByUserId(long userId, Pageable pageable);
}
