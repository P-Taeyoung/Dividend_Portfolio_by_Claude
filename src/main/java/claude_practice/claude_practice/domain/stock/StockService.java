package claude_practice.claude_practice.domain.stock;

import claude_practice.claude_practice.domain.stock.dto.StockRequestDto;
import claude_practice.claude_practice.domain.stock.dto.StockResponseDto;
import claude_practice.claude_practice.domain.stock.dto.StockSummaryDto;

import java.util.List;

public interface StockService {
    List<StockResponseDto> findAll();
    StockResponseDto findById(Long id);
    StockResponseDto findByTicker(String ticker);
    StockSummaryDto getSummary();
    StockResponseDto create(StockRequestDto dto);
    StockResponseDto update(Long id, StockRequestDto dto);
    void delete(Long id);
}
