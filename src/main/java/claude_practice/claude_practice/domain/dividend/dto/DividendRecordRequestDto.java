package claude_practice.claude_practice.domain.dividend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DividendRecordRequestDto {

    @NotNull
    private Long stockId;

    @NotNull
    private LocalDate receivedDate;

    @NotNull
    @Positive
    private BigDecimal amountPerShare;

    @NotNull
    @Positive
    private BigDecimal sharesHeld;

    @NotNull
    @Positive
    private BigDecimal totalAmount;

    private String currency;
    private String notes;
}
