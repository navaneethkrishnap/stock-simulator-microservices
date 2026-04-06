
## Features:
1. JWT tokens

## Code-level:

portfolio-service -> (Service-class) PortfolioService.java 
                  
public void redoStockDeductedFromAccount(DeductStocksRequestDTO deductStocksRequestDTO)

should take care of setting Average Holdings Price
( once stocks are sold, tracking of avgHoldingsPrice is lost and cannot be used again for rollback of selling in this method)

--
