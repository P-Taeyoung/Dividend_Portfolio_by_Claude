package claude_practice.claude_practice.domain.stock;

import claude_practice.claude_practice.domain.stock.dto.StockRequestDto;
import claude_practice.claude_practice.domain.stock.dto.StockResponseDto;
import claude_practice.claude_practice.domain.stock.dto.StockSummaryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public List<StockResponseDto> getAll() {
        return stockService.findAll();
    }

    @GetMapping("/{id}")
    public StockResponseDto getById(@PathVariable Long id) {
        return stockService.findById(id);
    }

    @GetMapping("/summary")
    public StockSummaryDto getSummary() {
        return stockService.getSummary();
    }

    @GetMapping("/search")
    public StockResponseDto searchByTicker(@RequestParam String ticker) {
        return stockService.findByTicker(ticker);
    }

    @PostMapping
    public ResponseEntity<StockResponseDto> create(@Valid @RequestBody StockRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.create(dto));
    }

    @PutMapping("/{id}")
    public StockResponseDto update(@PathVariable Long id, @Valid @RequestBody StockRequestDto dto) {
        return stockService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
