package claude_practice.claude_practice.domain.stock;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String ticker;

    @Column(nullable = false, length = 200)
    private String companyName;

    @Column(length = 100)
    private String sector;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal shares;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal avgPurchasePrice;

    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "USD";

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal dividendPerShare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DividendFrequency dividendFrequency;

    private LocalDate exDividendDate;
    private LocalDate paymentDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public BigDecimal annualDividendIncome() {
        return dividendPerShare
                .multiply(shares)
                .multiply(BigDecimal.valueOf(dividendFrequency.paymentsPerYear()))
                .setScale(4, RoundingMode.HALF_UP);
    }
}
