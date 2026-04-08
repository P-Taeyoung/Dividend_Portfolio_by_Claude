package claude_practice.claude_practice.domain.stock.dto;

import claude_practice.claude_practice.domain.stock.DividendFrequency;
import claude_practice.claude_practice.domain.stock.Stock;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class StockResponseDto {

    private Long id;
    private String ticker;
    private String companyName;
    private String sector;
    private BigDecimal shares;
    private BigDecimal avgPurchasePrice;
    private BigDecimal currentValue;
    private String currency;
    private BigDecimal dividendPerShare;
    private DividendFrequency dividendFrequency;
    private BigDecimal annualDividendIncome;
    private BigDecimal monthlyDividendIncome;
    private BigDecimal dividendYield;
    private LocalDate exDividendDate;
    private LocalDate paymentDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StockResponseDto from(Stock stock) {
        BigDecimal currentValue = stock.getAvgPurchasePrice().multiply(stock.getShares());
        BigDecimal annualIncome = stock.annualDividendIncome();
        BigDecimal monthlyIncome = annualIncome.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
        BigDecimal yield = currentValue.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : annualIncome.divide(currentValue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return StockResponseDto.builder()
                .id(stock.getId())
                .ticker(stock.getTicker())
                .companyName(stock.getCompanyName())
                .sector(stock.getSector())
                .shares(stock.getShares())
                .avgPurchasePrice(stock.getAvgPurchasePrice())
                .currentValue(currentValue.setScale(4, RoundingMode.HALF_UP))
                .currency(stock.getCurrency())
                .dividendPerShare(stock.getDividendPerShare())
                .dividendFrequency(stock.getDividendFrequency())
                .annualDividendIncome(annualIncome)
                .monthlyDividendIncome(monthlyIncome)
                .dividendYield(yield.setScale(2, RoundingMode.HALF_UP))
                .exDividendDate(stock.getExDividendDate())
                .paymentDate(stock.getPaymentDate())
                .notes(stock.getNotes())
                .createdAt(stock.getCreatedAt())
                .updatedAt(stock.getUpdatedAt())
                .build();
    }
}
