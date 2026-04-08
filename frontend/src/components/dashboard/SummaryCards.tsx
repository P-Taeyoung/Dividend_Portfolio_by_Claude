import type { StockSummary } from '../../types';
import styles from './SummaryCards.module.css';

interface Props {
  summary: StockSummary;
}

function fmt(value: number, decimals = 2) {
  return value.toLocaleString('ko-KR', { minimumFractionDigits: decimals, maximumFractionDigits: decimals });
}

export default function SummaryCards({ summary }: Props) {
  const cards = [
    { label: '보유 종목', value: `${summary.totalStocks}개`, sub: '총 종목 수' },
    { label: '월 배당 수입', value: `$${fmt(summary.totalMonthlyDividendIncome)}`, sub: '예상 월 배당금' },
    { label: '연 배당 수입', value: `$${fmt(summary.totalAnnualDividendIncome)}`, sub: '예상 연간 배당금' },
    { label: '포트폴리오 총액', value: `$${fmt(summary.totalPortfolioValue)}`, sub: '보유 종목 합산' },
    { label: '평균 배당수익률', value: `${fmt(summary.averageDividendYield)}%`, sub: '포트폴리오 기준' },
  ];

  return (
    <div className={styles.grid}>
      {cards.map(card => (
        <div key={card.label} className={styles.card}>
          <div className={styles.label}>{card.label}</div>
          <div className={styles.value}>{card.value}</div>
          <div className={styles.sub}>{card.sub}</div>
        </div>
      ))}
    </div>
  );
}
