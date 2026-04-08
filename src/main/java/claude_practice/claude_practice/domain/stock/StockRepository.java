package claude_practice.claude_practice.domain.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByTickerIgnoreCase(String ticker);
    boolean existsByTickerIgnoreCase(String ticker);
}
