import { useState } from 'react';
import type { DividendFrequency, StockRequest } from '../../types';
import styles from './StockForm.module.css';

interface Props {
  initial?: Partial<StockRequest>;
  onSubmit: (data: StockRequest) => void;
  onCancel: () => void;
  loading?: boolean;
}

const DEFAULT: StockRequest = {
  ticker: '',
  companyName: '',
  sector: '',
  shares: 0,
  avgPurchasePrice: 0,
  currency: 'USD',
  dividendPerShare: 0,
  dividendFrequency: 'QUARTERLY',
  notes: '',
};

export default function StockForm({ initial, onSubmit, onCancel, loading }: Props) {
  const [form, setForm] = useState<StockRequest>({ ...DEFAULT, ...initial });

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  }

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    onSubmit({
      ...form,
      ticker: form.ticker.toUpperCase(),
      shares: Number(form.shares),
      avgPurchasePrice: Number(form.avgPurchasePrice),
      dividendPerShare: Number(form.dividendPerShare),
    });
  }

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <div className={styles.row}>
        <label>
          티커 *
          <input name="ticker" value={form.ticker} onChange={handleChange} required placeholder="SCHD" />
        </label>
        <label>
          회사명 *
          <input name="companyName" value={form.companyName} onChange={handleChange} required placeholder="Schwab US Dividend ETF" />
        </label>
      </div>
      <div className={styles.row}>
        <label>
          섹터
          <input name="sector" value={form.sector ?? ''} onChange={handleChange} placeholder="ETF, 금융, IT ..." />
        </label>
        <label>
          통화
          <select name="currency" value={form.currency} onChange={handleChange}>
            <option value="USD">USD</option>
            <option value="KRW">KRW</option>
          </select>
        </label>
      </div>
      <div className={styles.row}>
        <label>
          보유 주수 *
          <input type="number" name="shares" value={form.shares} onChange={handleChange} required min="0.0001" step="0.0001" />
        </label>
        <label>
          평균 매입가 *
          <input type="number" name="avgPurchasePrice" value={form.avgPurchasePrice} onChange={handleChange} required min="0.0001" step="0.0001" />
        </label>
      </div>
      <div className={styles.row}>
        <label>
          주당 배당금 *
          <input type="number" name="dividendPerShare" value={form.dividendPerShare} onChange={handleChange} required min="0" step="0.0001" />
        </label>
        <label>
          배당 주기 *
          <select name="dividendFrequency" value={form.dividendFrequency} onChange={handleChange}>
            {(['MONTHLY', 'QUARTERLY', 'SEMI_ANNUAL', 'ANNUAL'] as DividendFrequency[]).map(f => (
              <option key={f} value={f}>
                {{ MONTHLY: '월배당', QUARTERLY: '분기배당', SEMI_ANNUAL: '반기배당', ANNUAL: '연배당' }[f]}
              </option>
            ))}
          </select>
        </label>
      </div>
      <div className={styles.row}>
        <label>
          배당락일
          <input type="date" name="exDividendDate" value={form.exDividendDate ?? ''} onChange={handleChange} />
        </label>
        <label>
          배당 지급일
          <input type="date" name="paymentDate" value={form.paymentDate ?? ''} onChange={handleChange} />
        </label>
      </div>
      <label>
        메모
        <textarea name="notes" value={form.notes ?? ''} onChange={handleChange} rows={2} />
      </label>
      <div className={styles.actions}>
        <button type="button" className={styles.cancel} onClick={onCancel}>취소</button>
        <button type="submit" className={styles.submit} disabled={loading}>
          {loading ? '저장 중...' : '저장'}
        </button>
      </div>
    </form>
  );
}
