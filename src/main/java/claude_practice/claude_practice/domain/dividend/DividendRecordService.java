package claude_practice.claude_practice.domain.dividend;

import claude_practice.claude_practice.domain.dividend.dto.DividendRecordRequestDto;
import claude_practice.claude_practice.domain.dividend.dto.DividendRecordResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DividendRecordService {
    Page<DividendRecordResponseDto> findAll(Pageable pageable);
    List<DividendRecordResponseDto> findByStockId(Long stockId);
    List<DividendRecordResponseDto> findByYearAndMonth(int year, int month);
    DividendRecordResponseDto create(DividendRecordRequestDto dto);
    void delete(Long id);
}
