package claude_practice.claude_practice.domain.dividend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DividendRecordRepository extends JpaRepository<DividendRecord, Long> {

    List<DividendRecord> findByStockIdOrderByReceivedDateDesc(Long stockId);

    @Query("SELECT d FROM DividendRecord d WHERE YEAR(d.receivedDate) = :year AND MONTH(d.receivedDate) = :month ORDER BY d.receivedDate DESC")
    List<DividendRecord> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    Page<DividendRecord> findAllByOrderByReceivedDateDesc(Pageable pageable);
}
