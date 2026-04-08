import DividendHistory from '../components/dividend/DividendHistory';
import styles from './DividendHistoryPage.module.css';

export default function DividendHistoryPage() {
  return (
    <div>
      <h2 className={styles.title}>배당 수령 기록</h2>
      <DividendHistory />
    </div>
  );
}
