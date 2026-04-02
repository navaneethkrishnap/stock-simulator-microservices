package com.NKP.order_service.service;

import com.NKP.order_service.dto.OrderPaymentRequestDTO;
import com.NKP.order_service.dto.OrderRequestDTO;
import com.NKP.order_service.dto.portfolfio.AddStocksRequestDTO;
import com.NKP.order_service.dto.stock.StockDTO;
import com.NKP.order_service.feign.PortfolioClient;
import com.NKP.order_service.feign.StockClient;
import com.NKP.order_service.feign.UserClient;
import com.NKP.order_service.model.Order;
import com.NKP.order_service.model.OrderStatus;
import com.NKP.order_service.model.OrderType;
import com.NKP.order_service.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlaceOrderService {

    private final StockClient stockClient;
    private final UserClient userClient;
    private final OrderRepository orderRepository;
    private final PortfolioClient portfolioClient;


    public void placeOrder(OrderRequestDTO orderRequestDTO){

        LocalDateTime time = LocalDateTime.now();

        long userId = orderRequestDTO.getUserId();
        String symbol = orderRequestDTO.getSymbol();
        OrderType type = orderRequestDTO.getType();


        StockDTO stock = stockClient.getStock(symbol);
        String stockName = stock.getName();
        BigDecimal marketPrice = stock.getCMP();
        // call user-service
        // validate sufficient funds
        // deduct the amount

        System.err.println(marketPrice);

        BigDecimal quantity = new BigDecimal(orderRequestDTO.getQuantity());
//        BigDecimal totalAmount = orderRequestDTO.getOrderPrice().multiply(quantity);
        BigDecimal totalAmount = marketPrice.multiply(quantity);

        Order order = Order.builder()
                .userId(userId)
                .orderPrice(marketPrice)
                .quantity(orderRequestDTO.getQuantity())
                .orderPlacedTime(time)
                .totalPrice(totalAmount)
                .type(type)
                .status(OrderStatus.PENDING)
                .build();

        System.err.println(order);

        orderRepository.save(order);

        System.err.println("db saved: " + order);


        OrderPaymentRequestDTO requestDTO = new OrderPaymentRequestDTO();
        requestDTO.setAmount(totalAmount);
        requestDTO.setUserId(userId);


        try{
            userClient.deductBuyOrderFunds(requestDTO);

            try{
                // update portfolio
                AddStocksRequestDTO addStocksRequestDTO = AddStocksRequestDTO
                        .builder()
                        .userId(userId)
                        .symbol(symbol)
                        .stockName(stockName)
                        .quantities(orderRequestDTO.getQuantity())
                        .orderPrice(marketPrice)
                        .build();

                portfolioClient.addStockIntoAccount(addStocksRequestDTO);

                // update order status
                order.setStatus(OrderStatus.EXECUTED);
                orderRepository.save(order);
                System.err.println("executed status");

            }catch (Exception portfolioEx){

                try{
                    userClient.refundBuyOrderPayment(requestDTO);
                    System.out.println("Refund Successfuly");
                } catch (Exception refundEx){
                    System.err.println("Refund failed!");
                }

                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);

                throw new RuntimeException("Portfolio Update failed. " + portfolioEx.getMessage(), portfolioEx);
            }

        }catch (Exception paymentsEx){
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            throw new RuntimeException("Payment failed: " + paymentsEx.getMessage(), paymentsEx);
        }

        orderRepository.save(order);
    }
}
