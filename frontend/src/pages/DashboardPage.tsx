import { useEffect, useState } from 'react';
import type { StockSummary } from '../types';
import { stockApi } from '../api/stockApi';
import SummaryCards from '../components/dashboard/SummaryCards';
import FrequencyBreakdown from '../components/dashboard/FrequencyBreakdown';
import styles from './DashboardPage.module.css';

export default function DashboardPage() {
  const [summary, setSummary] = useState<StockSummary | null>(null);
  const [error, setError] = useState('');

  useEffect(() => {
    stockApi.getSummary()
      .then(setSummary)
      .catch(() => setError('데이터를 불러오는데 실패했습니다. 서버 연결을 확인해주세요.'));
  }, []);

  if (error) return <p className={styles.error}>{error}</p>;
  if (!summary) return <p className={styles.loading}>로딩 중...</p>;

  return (
    <div>
      <h2 className={styles.title}>대시보드</h2>
      <SummaryCards summary={summary} />
      <div className={styles.grid}>
        <FrequencyBreakdown breakdown={summary.frequencyBreakdown} />
        {Object.keys(summary.sectorBreakdown).length > 0 && (
          <div className={styles.sectorCard}>
            <h3 className={styles.cardTitle}>섹터별 포트폴리오</h3>
            {Object.entries(summary.sectorBreakdown).map(([sector, value]) => (
              <div key={sector} className={styles.sectorRow}>
                <span>{sector}</span>
                <span>${value.toLocaleString('ko-KR', { maximumFractionDigits: 0 })}</span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
