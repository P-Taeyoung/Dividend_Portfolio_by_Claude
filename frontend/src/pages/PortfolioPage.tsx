import { useEffect, useState } from 'react';
import type { Stock, StockRequest } from '../types';
import { stockApi } from '../api/stockApi';
import StockList from '../components/stock/StockList';
import StockForm from '../components/stock/StockForm';
import styles from './PortfolioPage.module.css';

export default function PortfolioPage() {
  const [stocks, setStocks] = useState<Stock[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  async function loadStocks() {
    try {
      const data = await stockApi.getAll();
      setStocks(data);
    } catch {
      setError('종목 목록을 불러오는데 실패했습니다.');
    }
  }

  useEffect(() => { loadStocks(); }, []);

  async function handleCreate(data: StockRequest) {
    setLoading(true);
    try {
      await stockApi.create(data);
      setShowForm(false);
      loadStocks();
    } catch (e: unknown) {
      const msg = (e as { response?: { data?: { message?: string } } })?.response?.data?.message;
      alert(msg ?? '저장에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <div className={styles.header}>
        <h2 className={styles.title}>포트폴리오</h2>
        <button className={styles.addBtn} onClick={() => setShowForm(v => !v)}>
          {showForm ? '취소' : '+ 종목 추가'}
        </button>
      </div>

      {error && <p className={styles.error}>{error}</p>}

      {showForm && (
        <div className={styles.formCard}>
          <h3 className={styles.formTitle}>새 종목 추가</h3>
          <StockForm onSubmit={handleCreate} onCancel={() => setShowForm(false)} loading={loading} />
        </div>
      )}

      <StockList stocks={stocks} onRefresh={loadStocks} />
    </div>
  );
}
