import { useState } from 'react';
import type { Stock, StockRequest } from '../../types';
import { FREQUENCY_LABEL } from '../../types';
import { stockApi } from '../../api/stockApi';
import StockForm from './StockForm';
import styles from './StockList.module.css';

interface Props {
  stocks: Stock[];
  onRefresh: () => void;
}

function fmt(v: number, d = 2) {
  return v.toLocaleString('ko-KR', { minimumFractionDigits: d, maximumFractionDigits: d });
}

export default function StockList({ stocks, onRefresh }: Props) {
  const [editId, setEditId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);

  async function handleDelete(id: number, ticker: string) {
    if (!confirm(`${ticker} 종목을 삭제하시겠습니까?`)) return;
    await stockApi.delete(id);
    onRefresh();
  }

  async function handleUpdate(id: number, data: StockRequest) {
    setLoading(true);
    try {
      await stockApi.update(id, data);
      setEditId(null);
      onRefresh();
    } finally {
      setLoading(false);
    }
  }

  if (stocks.length === 0) {
    return <p className={styles.empty}>포트폴리오에 종목을 추가하세요.</p>;
  }

  return (
    <div className={styles.tableWrap}>
      <table className={styles.table}>
        <thead>
          <tr>
            <th>티커</th>
            <th>회사명</th>
            <th>섹터</th>
            <th>보유 주수</th>
            <th>평균 매입가</th>
            <th>주당 배당금</th>
            <th>배당 주기</th>
            <th>월 배당수입</th>
            <th>배당수익률</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          {stocks.map(stock => (
            editId === stock.id ? (
              <tr key={stock.id}>
                <td colSpan={10} className={styles.formCell}>
                  <StockForm
                    initial={stock}
                    onSubmit={data => handleUpdate(stock.id, data)}
                    onCancel={() => setEditId(null)}
                    loading={loading}
                  />
                </td>
              </tr>
            ) : (
              <tr key={stock.id}>
                <td><span className={styles.ticker}>{stock.ticker}</span></td>
                <td>{stock.companyName}</td>
                <td>{stock.sector ?? '-'}</td>
                <td>{fmt(stock.shares, 4)}</td>
                <td>${fmt(stock.avgPurchasePrice)}</td>
                <td>${fmt(stock.dividendPerShare, 4)}</td>
                <td>{FREQUENCY_LABEL[stock.dividendFrequency]}</td>
                <td className={styles.income}>${fmt(stock.monthlyDividendIncome)}</td>
                <td>{fmt(stock.dividendYield)}%</td>
                <td>
                  <button className={styles.editBtn} onClick={() => setEditId(stock.id)}>수정</button>
                  <button className={styles.delBtn} onClick={() => handleDelete(stock.id, stock.ticker)}>삭제</button>
                </td>
              </tr>
            )
          ))}
        </tbody>
      </table>
    </div>
  );
}
