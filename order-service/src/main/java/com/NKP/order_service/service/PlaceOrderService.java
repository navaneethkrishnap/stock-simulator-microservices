package com.NKP.order_service.service;

import com.NKP.order_service.dto.OrderPaymentRequestDTO;
import com.NKP.order_service.dto.OrderRequestDTO;
import com.NKP.order_service.dto.OrderResponseDTO;
import com.NKP.order_service.dto.portfolfio.AddStocksRequestDTO;
import com.NKP.order_service.dto.portfolfio.DeductStocksRequestDTO;
import com.NKP.order_service.dto.stock.StockDTO;
import com.NKP.order_service.feign.PortfolioClient;
import com.NKP.order_service.feign.StockClient;
import com.NKP.order_service.feign.UserClient;
import com.NKP.order_service.model.Order;
import com.NKP.order_service.model.OrderStatus;
import com.NKP.order_service.model.OrderType;
import com.NKP.order_service.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j // added validation
@Service
@RequiredArgsConstructor
public class PlaceOrderService {

    private final StockClient stockClient;
    private final UserClient userClient;
    private final OrderRepository orderRepository;
    private final PortfolioClient portfolioClient;


    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO){

        if(orderRequestDTO.getQuantity() < 1){
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        LocalDateTime time = LocalDateTime.now();
        long userId = orderRequestDTO.getUserId();
        String symbol = orderRequestDTO.getSymbol();
        OrderType type = orderRequestDTO.getType();
        long quantity = orderRequestDTO.getQuantity();

        StockDTO stock = stockClient.getStock(symbol);

        if(stock == null){
            throw new IllegalArgumentException("Stock not found: " + symbol);
        }

        String stockName = stock.getName();
        BigDecimal marketPrice = stock.getCMP();

        BigDecimal quantityBD = BigDecimal.valueOf(quantity);
        BigDecimal totalAmount = marketPrice.multiply(quantityBD);

        Order order = Order.builder()
                .userId(userId)
                .orderPrice(marketPrice)
                .quantity(quantity)
                .orderPlacedTime(time)
                .totalPrice(totalAmount)
                .type(type)
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);

        OrderPaymentRequestDTO requestDTO = new OrderPaymentRequestDTO();
        requestDTO.setAmount(totalAmount);
        requestDTO.setUserId(userId);

        boolean paymentDeducted = false;

        try{
            userClient.deductBuyOrderFunds(requestDTO);
            paymentDeducted = true;

            // update portfolio
            AddStocksRequestDTO addStocksRequestDTO = AddStocksRequestDTO
                    .builder()
                    .userId(userId)
                    .symbol(symbol)
                    .stockName(stockName)
                    .quantities(quantity)
                    .orderPrice(marketPrice)
                    .build();

            portfolioClient.addStockIntoAccount(addStocksRequestDTO);
            order.setStatus(OrderStatus.EXECUTED);

        }catch (Exception e){
            log.warn("Buy order failed for userId={}, symbol={}: {}",userId,symbol, e.getMessage(),e);

            if(paymentDeducted){
                try{
                    userClient.refundBuyOrderPayment(requestDTO);
                } catch (Exception refundEx){
                    log.error("REFUND FAILED for userId={}, symbol={}, amount={}: {}", userId,
                            symbol, totalAmount, refundEx.getMessage(), refundEx);
                    order.setStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order);
                    throw new RuntimeException("REFUND FAILED", refundEx);
                }
            }
            order.setStatus(OrderStatus.CANCELLED);
        }
        orderRepository.save(order);
        return OrderResponseDTO.builder()
                .orderPrice(marketPrice)
                .stockName(stockName)
                .symbol(symbol)
                .status(order.getStatus())
                .type(type)
                .totalInvestment(totalAmount)
                .quantity(quantity)
                .build();
    }

    public OrderResponseDTO sellOrder(OrderRequestDTO orderRequestDTO){

        if(orderRequestDTO.getQuantity() < 1){
            throw new IllegalArgumentException("Quantity must be positive");
        }

        LocalDateTime time = LocalDateTime.now();
        long userId = orderRequestDTO.getUserId();
        OrderType type = orderRequestDTO.getType();
        String symbol = orderRequestDTO.getSymbol();
        long quantity = orderRequestDTO.getQuantity();

        StockDTO stock = stockClient.getStock(symbol);
        if(stock == null){
            throw new IllegalArgumentException("Stock not found: " + symbol);
        }

        String stockName = stock.getName();
        BigDecimal marketPrice = stock.getCMP();

        BigDecimal totalAmtBD = BigDecimal.valueOf(quantity).multiply(marketPrice);

        Order order = Order
                .builder()
                .userId(userId)
                .status(OrderStatus.PENDING)
                .type(type)
                .orderPlacedTime(time)
                .orderPrice(marketPrice)
                .quantity(quantity)
                .totalPrice(totalAmtBD)
                .build();

        orderRepository.save(order);

        DeductStocksRequestDTO deductStocksRequestDTO = DeductStocksRequestDTO
                .builder()
                .userId(userId)
                .quantities(quantity)
                .stockName(stockName)
                .symbol(symbol)
                .build();

        boolean deducted = false;

        try{
            portfolioClient.deductStockFromAccount(deductStocksRequestDTO);
            deducted = true;

            OrderPaymentRequestDTO paymentRequestDTO = new OrderPaymentRequestDTO();
            paymentRequestDTO.setAmount(totalAmtBD);
            paymentRequestDTO.setUserId(userId);

            userClient.receiveSellOrderFunds(paymentRequestDTO);

            order.setStatus(OrderStatus.EXECUTED);

        }catch (Exception e){
            log.warn("Sell order failed for userId={}. symbol={}: {}", userId,symbol, e.getMessage(),e);
            if(deducted){
                try{
                    portfolioClient.redoStockDeductedFromAccount(deductStocksRequestDTO);
                }catch (Exception rollbackEx){
                    log.error("SELL ROLLBACK FAILED for userId={}, symbol={}, quantity={}: {}", userId,symbol,quantity,
                            rollbackEx.getMessage(), rollbackEx);
                    order.setStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order);
                    throw new RuntimeException("Sell rollback error", rollbackEx);
                }
            }
            order.setStatus(OrderStatus.CANCELLED);
        }
        orderRepository.save(order);
        return OrderResponseDTO.builder()
                .orderPrice(marketPrice)
                .stockName(stockName)
                .symbol(symbol)
                .status(order.getStatus())
                .type(type)
                .totalInvestment(totalAmtBD)
                .quantity(quantity)
                .build();
    }
}
