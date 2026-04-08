package claude_practice.claude_practice.domain.dividend;

import claude_practice.claude_practice.domain.dividend.dto.DividendRecordRequestDto;
import claude_practice.claude_practice.domain.dividend.dto.DividendRecordResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dividends")
@RequiredArgsConstructor
public class DividendRecordController {

    private final DividendRecordService dividendRecordService;

    @GetMapping
    public Page<DividendRecordResponseDto> getAll(
            @PageableDefault(size = 20, sort = "receivedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return dividendRecordService.findAll(pageable);
    }

    @GetMapping("/stock/{stockId}")
    public List<DividendRecordResponseDto> getByStockId(@PathVariable Long stockId) {
        return dividendRecordService.findByStockId(stockId);
    }

    @GetMapping("/monthly")
    public List<DividendRecordResponseDto> getByMonth(
            @RequestParam int year,
            @RequestParam int month) {
        return dividendRecordService.findByYearAndMonth(year, month);
    }

    @PostMapping
    public ResponseEntity<DividendRecordResponseDto> create(@Valid @RequestBody DividendRecordRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dividendRecordService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dividendRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
