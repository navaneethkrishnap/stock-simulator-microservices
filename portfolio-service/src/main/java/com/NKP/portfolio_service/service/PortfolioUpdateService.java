package com.NKP.portfolio_service.service;

import com.NKP.portfolio_service.dto.AddStocksRequestDTO;
import com.NKP.portfolio_service.model.Portfolio;
import com.NKP.portfolio_service.repo.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioUpdateService {
    private final PortfolioRepository portfolioRepository;


//    @Transactional
//    public void addStockIntoAccount(AddStocksRequestDTO requestDTO){
//
//        // check whether stock already exists;
//        // if exists update average price and also stock quantity for each stock
//
//        long userId = requestDTO.getUserId();
//        String stockSymbol = requestDTO.getSymbol();
//
//        long newQuantity = requestDTO.getQuantities();
//        BigDecimal newPrice = requestDTO.getOrderPrice();
//
//        Optional<Portfolio> existingSymbolAndUser =
//                portfolioRepository.findByUserIdAndSymbol(userId,stockSymbol);
//
//        if(existingSymbolAndUser.isPresent()){
//            Portfolio existingData = existingSymbolAndUser.get();
//
//            long oldQty = existingData.getQuantities();
//            BigDecimal oldAvgPrice = existingData.getAvgHoldingsPrice();
//
//            BigDecimal oldQtyBD = BigDecimal.valueOf(oldQty);
//            BigDecimal newQtyBD = BigDecimal.valueOf(newQuantity);
//
//            BigDecimal oldInvestment = oldAvgPrice.multiply(oldQtyBD);
//            BigDecimal newInvestment = newPrice.multiply(newQtyBD);
//
//            BigDecimal totalInvestment = oldInvestment.add(newInvestment);
//            BigDecimal totalQuantity = oldQtyBD.add(newQtyBD);
//
//            BigDecimal newAvgPrice = totalInvestment.divide(totalQuantity,2, RoundingMode.HALF_UP);
//
//            existingData.setQuantities(oldQty + newQuantity);
//            existingData.setAvgHoldingsPrice(newAvgPrice);
//
//            portfolioRepository.save(existingData);
//        } else{
//            Portfolio portfolio = Portfolio.builder()
//                    .stockName(requestDTO.getStockName())
//                    .symbol(stockSymbol)
//                    .userId(requestDTO.getUserId())
//                    .quantities(requestDTO.getQuantities())
//                    .avgHoldingsPrice(requestDTO.getOrderPrice())
//                    .build();
//            portfolioRepository.save(portfolio);
//        }
//    }

    @Transactional
    public void addStockIntoAccount(AddStocksRequestDTO requestDTO){

        long userId = requestDTO.getUserId();
        String stockSymbol = requestDTO.getSymbol();

        long newQuantity = requestDTO.getQuantities();
        BigDecimal newPrice = requestDTO.getOrderPrice();

        Optional<Portfolio> existingOpt = portfolioRepository
                .findByUserIdAndSymbol(userId, stockSymbol);

        if(existingOpt.isPresent()) {

            Portfolio existing = existingOpt.get();

            long oldQty = existing.getQuantities();
            BigDecimal oldTotalInvestment = existing.getTotalInvestment();

            BigDecimal newInvestment =
                    newPrice.multiply(BigDecimal.valueOf(newQuantity));

            BigDecimal updatedTotalInvestment =
                    oldTotalInvestment.add(newInvestment);

            long updatedQty = oldQty + newQuantity;

            BigDecimal newAvgPrice =
                    updatedTotalInvestment
                            .divide(BigDecimal.valueOf(updatedQty), 2, RoundingMode.HALF_UP);

            existing.setQuantities(updatedQty);
            existing.setTotalInvestment(updatedTotalInvestment);
            existing.setAvgHoldingsPrice(newAvgPrice);

            portfolioRepository.save(existing);

        } else {

            BigDecimal totalInvestment =
                    newPrice.multiply(BigDecimal.valueOf(newQuantity));

            Portfolio portfolio = Portfolio.builder()
                    .stockName(requestDTO.getStockName())
                    .symbol(stockSymbol)
                    .userId(userId)
                    .quantities(newQuantity)
                    .avgHoldingsPrice(newPrice)
                    .totalInvestment(totalInvestment)
                    .build();

            portfolioRepository.save(portfolio);
        }
    }
}
