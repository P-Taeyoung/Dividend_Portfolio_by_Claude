package claude_practice.claude_practice.domain.stock;

import claude_practice.claude_practice.domain.stock.dto.StockRequestDto;
import claude_practice.claude_practice.domain.stock.dto.StockResponseDto;
import claude_practice.claude_practice.domain.stock.dto.StockSummaryDto;
import claude_practice.claude_practice.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public List<StockResponseDto> findAll() {
        return stockRepository.findAll().stream()
                .map(StockResponseDto::from)
                .toList();
    }

    @Override
    public StockResponseDto findById(Long id) {
        return StockResponseDto.from(getStock(id));
    }

    @Override
    public StockResponseDto findByTicker(String ticker) {
        Stock stock = stockRepository.findByTickerIgnoreCase(ticker)
                .orElseThrow(() -> new ResourceNotFoundException("종목을 찾을 수 없습니다: " + ticker));
        return StockResponseDto.from(stock);
    }

    @Override
    public StockSummaryDto getSummary() {
        List<Stock> stocks = stockRepository.findAll();

        BigDecimal totalPortfolioValue = stocks.stream()
                .map(s -> s.getAvgPurchasePrice().multiply(s.getShares()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAnnualIncome = stocks.stream()
                .map(Stock::annualDividendIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMonthlyIncome = totalAnnualIncome.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);

        BigDecimal avgYield = totalPortfolioValue.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : totalAnnualIncome.divide(totalPortfolioValue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);

        Map<String, BigDecimal> frequencyBreakdown = stocks.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getDividendFrequency().name(),
                        Collectors.mapping(Stock::annualDividendIncome,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        Map<String, BigDecimal> sectorBreakdown = stocks.stream()
                .filter(s -> s.getSector() != null && !s.getSector().isBlank())
                .collect(Collectors.groupingBy(
                        Stock::getSector,
                        Collectors.mapping(
                                s -> s.getAvgPurchasePrice().multiply(s.getShares()),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        return StockSummaryDto.builder()
                .totalStocks(stocks.size())
                .totalPortfolioValue(totalPortfolioValue.setScale(4, RoundingMode.HALF_UP))
                .totalAnnualDividendIncome(totalAnnualIncome)
                .totalMonthlyDividendIncome(totalMonthlyIncome)
                .averageDividendYield(avgYield)
                .frequencyBreakdown(frequencyBreakdown)
                .sectorBreakdown(sectorBreakdown)
                .build();
    }

    @Override
    @Transactional
    public StockResponseDto create(StockRequestDto dto) {
        Stock stock = Stock.builder()
                .ticker(dto.getTicker().toUpperCase())
                .companyName(dto.getCompanyName())
                .sector(dto.getSector())
                .shares(dto.getShares())
                .avgPurchasePrice(dto.getAvgPurchasePrice())
                .currency(dto.getCurrency())
                .dividendPerShare(dto.getDividendPerShare())
                .dividendFrequency(dto.getDividendFrequency())
                .exDividendDate(dto.getExDividendDate())
                .paymentDate(dto.getPaymentDate())
                .notes(dto.getNotes())
                .build();
        return StockResponseDto.from(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public StockResponseDto update(Long id, StockRequestDto dto) {
        Stock stock = getStock(id);
        stock.setTicker(dto.getTicker().toUpperCase());
        stock.setCompanyName(dto.getCompanyName());
        stock.setSector(dto.getSector());
        stock.setShares(dto.getShares());
        stock.setAvgPurchasePrice(dto.getAvgPurchasePrice());
        stock.setCurrency(dto.getCurrency());
        stock.setDividendPerShare(dto.getDividendPerShare());
        stock.setDividendFrequency(dto.getDividendFrequency());
        stock.setExDividendDate(dto.getExDividendDate());
        stock.setPaymentDate(dto.getPaymentDate());
        stock.setNotes(dto.getNotes());
        return StockResponseDto.from(stock);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Stock stock = getStock(id);
        stockRepository.delete(stock);
    }

    private Stock getStock(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("종목을 찾을 수 없습니다: id=" + id));
    }
}
