package claude_practice.claude_practice.domain.stock.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
public class StockSummaryDto {

    private int totalStocks;
    private BigDecimal totalPortfolioValue;
    private BigDecimal totalAnnualDividendIncome;
    private BigDecimal totalMonthlyDividendIncome;
    private BigDecimal averageDividendYield;
    private Map<String, BigDecimal> frequencyBreakdown;
    private Map<String, BigDecimal> sectorBreakdown;
}
