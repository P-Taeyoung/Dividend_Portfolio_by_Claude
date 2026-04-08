package claude_practice.claude_practice.domain.dividend;

import claude_practice.claude_practice.domain.dividend.dto.DividendRecordRequestDto;
import claude_practice.claude_practice.domain.dividend.dto.DividendRecordResponseDto;
import claude_practice.claude_practice.domain.stock.Stock;
import claude_practice.claude_practice.domain.stock.StockRepository;
import claude_practice.claude_practice.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DividendRecordServiceImpl implements DividendRecordService {

    private final DividendRecordRepository dividendRecordRepository;
    private final StockRepository stockRepository;

    @Override
    public Page<DividendRecordResponseDto> findAll(Pageable pageable) {
        return dividendRecordRepository.findAllByOrderByReceivedDateDesc(pageable)
                .map(DividendRecordResponseDto::from);
    }

    @Override
    public List<DividendRecordResponseDto> findByStockId(Long stockId) {
        return dividendRecordRepository.findByStockIdOrderByReceivedDateDesc(stockId).stream()
                .map(DividendRecordResponseDto::from)
                .toList();
    }

    @Override
    public List<DividendRecordResponseDto> findByYearAndMonth(int year, int month) {
        return dividendRecordRepository.findByYearAndMonth(year, month).stream()
                .map(DividendRecordResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public DividendRecordResponseDto create(DividendRecordRequestDto dto) {
        Stock stock = stockRepository.findById(dto.getStockId())
                .orElseThrow(() -> new ResourceNotFoundException("종목을 찾을 수 없습니다: id=" + dto.getStockId()));

        DividendRecord record = DividendRecord.builder()
                .stock(stock)
                .receivedDate(dto.getReceivedDate())
                .amountPerShare(dto.getAmountPerShare())
                .sharesHeld(dto.getSharesHeld())
                .totalAmount(dto.getTotalAmount())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : stock.getCurrency())
                .notes(dto.getNotes())
                .build();

        return DividendRecordResponseDto.from(dividendRecordRepository.save(record));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        DividendRecord record = dividendRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("배당 기록을 찾을 수 없습니다: id=" + id));
        dividendRecordRepository.delete(record);
    }
}
