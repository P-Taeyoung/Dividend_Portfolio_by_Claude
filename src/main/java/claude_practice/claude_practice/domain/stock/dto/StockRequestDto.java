package claude_practice.claude_practice.domain.stock.dto;

import claude_practice.claude_practice.domain.stock.DividendFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class StockRequestDto {

    @NotBlank
    private String ticker;

    @NotBlank
    private String companyName;

    private String sector;

    @NotNull
    @Positive
    private BigDecimal shares;

    @NotNull
    @Positive
    private BigDecimal avgPurchasePrice;

    @NotBlank
    private String currency;

    @NotNull
    @PositiveOrZero
    private BigDecimal dividendPerShare;

    @NotNull
    private DividendFrequency dividendFrequency;

    private LocalDate exDividendDate;
    private LocalDate paymentDate;
    private String notes;
}
