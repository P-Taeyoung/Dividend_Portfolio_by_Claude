package claude_practice.claude_practice.domain.dividend.dto;

import claude_practice.claude_practice.domain.dividend.DividendRecord;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class DividendRecordResponseDto {

    private Long id;
    private Long stockId;
    private String ticker;
    private String companyName;
    private LocalDate receivedDate;
    private BigDecimal amountPerShare;
    private BigDecimal sharesHeld;
    private BigDecimal totalAmount;
    private String currency;
    private String notes;
    private LocalDateTime createdAt;

    public static DividendRecordResponseDto from(DividendRecord record) {
        return DividendRecordResponseDto.builder()
                .id(record.getId())
                .stockId(record.getStock().getId())
                .ticker(record.getStock().getTicker())
                .companyName(record.getStock().getCompanyName())
                .receivedDate(record.getReceivedDate())
                .amountPerShare(record.getAmountPerShare())
                .sharesHeld(record.getSharesHeld())
                .totalAmount(record.getTotalAmount())
                .currency(record.getCurrency())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
