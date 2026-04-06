package com.NKP.portfolio_service.service;

import com.NKP.portfolio_service.dto.AddStocksRequestDTO;
import com.NKP.portfolio_service.dto.DeductStocksRequestDTO;
import com.NKP.portfolio_service.model.Portfolio;
import com.NKP.portfolio_service.repo.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.introspect.PotentialCreator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioUpdateService {
    private final PortfolioRepository portfolioRepository;

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

    public void rollBackStockAddition(){
        System.out.println("To be implemented");
    }

    @Transactional
    public void deductStockFromAccount(DeductStocksRequestDTO requestDTO){
        // extract values
        String symbol = requestDTO.getSymbol();
        String stockName = requestDTO.getStockName();
        long userId = requestDTO.getUserId();
        long sellQuantity = requestDTO.getQuantities();

        if(sellQuantity < 1){
            throw new IllegalArgumentException("Sell quantity must be at least 1");
        }

        Portfolio portfolio = portfolioRepository
                .findByUserIdAndSymbolAndStockName(userId,symbol,stockName)
                .orElseThrow(()-> new IllegalStateException("No holdings found."));

        applySellToPortfolio(portfolio, sellQuantity);
    }

    private void applySellToPortfolio(Portfolio portfolio, long sellQuantity) {

        if(sellQuantity > portfolio.getQuantities()){
            throw new IllegalStateException("Insufficient stock holdings");
        }

        long updatedQty = portfolio.getQuantities() - sellQuantity;
        portfolio.setQuantities(updatedQty);

        if(updatedQty == 0){
            portfolio.setTotalInvestment(BigDecimal.ZERO);
            portfolio.setAvgHoldingsPrice(BigDecimal.ZERO);
            return;
        }

        BigDecimal totalInvestmentReduction = BigDecimal.valueOf(sellQuantity)
                .multiply(portfolio.getAvgHoldingsPrice());
        BigDecimal updatedTotalInvestment = portfolio.getTotalInvestment().subtract(totalInvestmentReduction);
        if(updatedTotalInvestment.compareTo(BigDecimal.ZERO) < 0){
            updatedTotalInvestment = BigDecimal.ZERO;
        }
        portfolio.setTotalInvestment(updatedTotalInvestment);
    }


    // should take care of setting Average Holdings Price
    // when rollback of selling happens
    // avgHoldingsPrice is not rolled back and inconsistency happens
    @Transactional
    public void redoStockDeductedFromAccount(DeductStocksRequestDTO deductStocksRequestDTO) {
        long userId = deductStocksRequestDTO.getUserId();
        String symbol = deductStocksRequestDTO.getSymbol();
        String stockName = deductStocksRequestDTO.getStockName();
        long quantityToRedo = deductStocksRequestDTO.getQuantities();

        Portfolio portfolio = portfolioRepository.findByUserIdAndSymbolAndStockName(userId,symbol,stockName)
                .orElseThrow(()-> new IllegalStateException("No Holdings found"));

        /*
        Problem >>> Cannot get average Holdings price
                    because there no such data exists
                    once sold
                    need some way to store
                    sold stocks and their average holdings value of all stock
         */
        BigDecimal avgHoldingsPrice = portfolio.getAvgHoldingsPrice();
        BigDecimal totalInvestment = BigDecimal.valueOf(quantityToRedo).multiply(avgHoldingsPrice);

        portfolio.setQuantities(portfolio.getQuantities() + quantityToRedo);
        portfolio.setTotalInvestment(portfolio.getTotalInvestment().add(totalInvestment));
        portfolio.setAvgHoldingsPrice(avgHoldingsPrice);

        portfolioRepository.save(portfolio);
    }
}
