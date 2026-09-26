package com.NKP.portfolio_service.service;

import com.NKP.portfolio_service.dto.AddStocksRequestDTO;
import com.NKP.portfolio_service.dto.DeductStocksRequestDTO;
import com.NKP.portfolio_service.model.Portfolio;
import com.NKP.portfolio_service.model.PortfolioTransaction;
import com.NKP.portfolio_service.model.TransactionType;
import com.NKP.portfolio_service.repo.PortfolioRepository;
import com.NKP.portfolio_service.repo.PortfolioTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioUpdateService {
    private final PortfolioRepository portfolioRepository;
    private final PortfolioTransactionRepository transactionRepository;

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

        BigDecimal investmentDelta = newPrice.multiply(BigDecimal.valueOf(newQuantity));
        PortfolioTransaction ledgerEntry = PortfolioTransaction.builder()
                .orderId(requestDTO.getOrderId())
                .userId(userId)
                .symbol(stockSymbol)
                .type(TransactionType.BUY)
                .quantityDelta(newQuantity)
                .investmentDelta(investmentDelta)
                .reversed(false)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(ledgerEntry);
    }


    @Transactional
    public void deductStockFromAccount(DeductStocksRequestDTO requestDTO){

        Long orderId = requestDTO.getOrderId();
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

        BigDecimal investmentReduction = applySellToPortfolio(portfolio,sellQuantity);
        portfolioRepository.save(portfolio);

        PortfolioTransaction ledgerEntry = PortfolioTransaction.builder()
                .orderId(orderId)
                .userId(userId)
                .symbol(symbol)
                .type(TransactionType.SELL)
                .quantityDelta(sellQuantity)
                .investmentDelta(investmentReduction)
                .reversed(false)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(ledgerEntry);
    }

    private BigDecimal applySellToPortfolio(Portfolio portfolio, long sellQuantity) {

        if(sellQuantity > portfolio.getQuantities()){
            throw new IllegalStateException("Insufficient stock holdings");
        }

        long updatedQty = portfolio.getQuantities() - sellQuantity;
        portfolio.setQuantities(updatedQty);

        BigDecimal totalInvestmentReduction = BigDecimal.valueOf(sellQuantity)
                .multiply(portfolio.getAvgHoldingsPrice());

        BigDecimal oldTotalInvestment = portfolio.getTotalInvestment();
        BigDecimal updatedTotalInvestment = oldTotalInvestment.subtract(totalInvestmentReduction);

        if(updatedQty == 0 || updatedTotalInvestment.compareTo(BigDecimal.ZERO) < 0){
            updatedTotalInvestment = BigDecimal.ZERO;
        }
        portfolio.setTotalInvestment(updatedTotalInvestment);

        return oldTotalInvestment.subtract(updatedTotalInvestment);
    }


    // should take care of setting Average Holdings Price
    // when rollback of selling happens
    // avgHoldingsPrice is not rolled back and inconsistency happens
    @Transactional
    public void redoStockDeductedFromAccount(DeductStocksRequestDTO deductStocksRequestDTO) {

        Long orderId = deductStocksRequestDTO.getOrderId();

        Optional<PortfolioTransaction> ledgerEntryOpt =
                transactionRepository.findByOrderId(orderId);

        if(ledgerEntryOpt.isEmpty()){
            log.info("No portfolio transaction found for orderId={}. nothing to reverse (sell)", orderId);
            return;
        }

        PortfolioTransaction ledgerEntry = ledgerEntryOpt.get();

        if(ledgerEntry.getType() != TransactionType.SELL){
            log.warn("Ledger entry for orderId={} is not a SELL transaction (found{}), skipping reversal", orderId,ledgerEntry.getType());
            return;
        }

        if(ledgerEntry.isReversed()){
            log.info("Portfolio transaction for orderId={} already reversed, skipping", orderId);
            return;
        }

        Portfolio portfolio = portfolioRepository
                .findByUserIdAndSymbolAndStockName(ledgerEntry.getUserId(),
                        ledgerEntry.getSymbol(),
                        deductStocksRequestDTO.getStockName())
                .orElseThrow(()->new IllegalStateException("Portfolio row missing despite ledger entry exists"));

        long restoredQty = portfolio.getQuantities() + ledgerEntry.getQuantityDelta();
        BigDecimal restoredTotalInvestment = portfolio.getTotalInvestment().add(ledgerEntry.getInvestmentDelta());

        portfolio.setQuantities(restoredQty);
        portfolio.setTotalInvestment(restoredTotalInvestment);

        if(restoredQty > 0){
            BigDecimal restoredAvgPrice = restoredTotalInvestment
                    .divide(BigDecimal.valueOf(restoredQty),2, RoundingMode.HALF_UP);
            portfolio.setAvgHoldingsPrice(restoredAvgPrice);
        }
        portfolioRepository.save(portfolio);

        ledgerEntry.setReversed(true);
        transactionRepository.save(ledgerEntry);
    }

    @Transactional
    public void redoStockAddedIntoAccount(AddStocksRequestDTO addStocksRequestDTO) {
        Long orderId = addStocksRequestDTO.getOrderId();

        Optional<PortfolioTransaction> ledgerEntryOpt =
                transactionRepository.findByOrderId(orderId);

        if(ledgerEntryOpt.isEmpty()){
            log.info("No portfolio transaction found for orderId={}, nothing to reverse (buy)", orderId);
            return;
        }

        PortfolioTransaction ledgerEntry = ledgerEntryOpt.get();

        if(ledgerEntry.getType() != TransactionType.BUY){
            log.warn("Ledger entry for orderId={} is not a BUY transaction (found {}), skipping reversal",
                    orderId, ledgerEntry.getType());
            return;
        }

        if(ledgerEntry.isReversed()){
            log.info("Portfolio transaction for orderId={} already reversed, skipping",orderId);
            return;
        }

        Portfolio portfolio = portfolioRepository
                .findByUserIdAndSymbolAndStockName(ledgerEntry.getUserId(),ledgerEntry.getSymbol(),
                        addStocksRequestDTO.getStockName())
                .orElseThrow(() -> new IllegalStateException("Portfolio row missing despite ledger entry existing"));

        portfolio.setQuantities(portfolio.getQuantities() - ledgerEntry.getQuantityDelta());
        portfolio.setTotalInvestment(portfolio.getTotalInvestment().subtract(ledgerEntry.getInvestmentDelta()));

        if(portfolio.getQuantities() > 0){
            BigDecimal newAvgPrice = portfolio.getTotalInvestment()
                    .divide(BigDecimal.valueOf(portfolio.getQuantities()),2,RoundingMode.HALF_UP);
            portfolio.setAvgHoldingsPrice(newAvgPrice);
        }

        portfolioRepository.save(portfolio);

        ledgerEntry.setReversed(true);
        transactionRepository.save(ledgerEntry);
    }
}
