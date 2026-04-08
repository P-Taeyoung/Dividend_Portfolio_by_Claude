import { FREQUENCY_LABEL } from '../../types';
import styles from './FrequencyBreakdown.module.css';

interface Props {
  breakdown: Record<string, number>;
}

const COLORS: Record<string, string> = {
  MONTHLY: '#4caf50',
  QUARTERLY: '#2196f3',
  SEMI_ANNUAL: '#ff9800',
  ANNUAL: '#9c27b0',
};

export default function FrequencyBreakdown({ breakdown }: Props) {
  const total = Object.values(breakdown).reduce((a, b) => a + b, 0);

  return (
    <div className={styles.card}>
      <h3 className={styles.title}>배당 주기별 연간 수입</h3>
      {Object.entries(breakdown).map(([key, value]) => {
        const pct = total > 0 ? (value / total) * 100 : 0;
        return (
          <div key={key} className={styles.row}>
            <span className={styles.label}>{FREQUENCY_LABEL[key as keyof typeof FREQUENCY_LABEL] ?? key}</span>
            <div className={styles.barWrap}>
              <div
                className={styles.bar}
                style={{ width: `${pct}%`, background: COLORS[key] ?? '#888' }}
              />
            </div>
            <span className={styles.amount}>${value.toLocaleString('ko-KR', { maximumFractionDigits: 2 })}</span>
            <span className={styles.pct}>{pct.toFixed(1)}%</span>
          </div>
        );
      })}
      {Object.keys(breakdown).length === 0 && (
        <p className={styles.empty}>데이터가 없습니다.</p>
      )}
    </div>
  );
}
