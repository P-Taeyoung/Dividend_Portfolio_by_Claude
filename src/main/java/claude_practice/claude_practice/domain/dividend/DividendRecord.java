package claude_practice.claude_practice.domain.dividend;

import claude_practice.claude_practice.domain.stock.Stock;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dividend_records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DividendRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private LocalDate receivedDate;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal amountPerShare;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal sharesHeld;

    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "USD";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
