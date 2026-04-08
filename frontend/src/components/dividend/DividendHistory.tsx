import { useState, useEffect } from 'react';
import type { DividendRecord, PageResponse } from '../../types';
import { dividendApi } from '../../api/dividendApi';
import styles from './DividendHistory.module.css';

function fmt(v: number, d = 2) {
  return v.toLocaleString('ko-KR', { minimumFractionDigits: d, maximumFractionDigits: d });
}

export default function DividendHistory() {
  const [data, setData] = useState<PageResponse<DividendRecord> | null>(null);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);

  async function load(p: number) {
    setLoading(true);
    try {
      const result = await dividendApi.getAll(p, 20);
      setData(result);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(page); }, [page]);

  async function handleDelete(id: number) {
    if (!confirm('이 배당 기록을 삭제하시겠습니까?')) return;
    await dividendApi.delete(id);
    load(page);
  }

  return (
    <div>
      {loading && <p className={styles.loading}>로딩 중...</p>}
      {data && (
        <>
          <div className={styles.tableWrap}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>티커</th>
                  <th>회사명</th>
                  <th>수령일</th>
                  <th>주당 배당금</th>
                  <th>보유 주수</th>
                  <th>총 수령액</th>
                  <th>통화</th>
                  <th>작업</th>
                </tr>
              </thead>
              <tbody>
                {data.content.map(rec => (
                  <tr key={rec.id}>
                    <td><span className={styles.ticker}>{rec.ticker}</span></td>
                    <td>{rec.companyName}</td>
                    <td>{rec.receivedDate}</td>
                    <td>${fmt(rec.amountPerShare, 4)}</td>
                    <td>{fmt(rec.sharesHeld, 4)}</td>
                    <td className={styles.total}>${fmt(rec.totalAmount)}</td>
                    <td>{rec.currency}</td>
                    <td>
                      <button className={styles.delBtn} onClick={() => handleDelete(rec.id)}>삭제</button>
                    </td>
                  </tr>
                ))}
                {data.content.length === 0 && (
                  <tr><td colSpan={8} className={styles.empty}>배당 기록이 없습니다.</td></tr>
                )}
              </tbody>
            </table>
          </div>
          <div className={styles.pagination}>
            <button disabled={page === 0} onClick={() => setPage(p => p - 1)}>이전</button>
            <span>{page + 1} / {data.totalPages || 1}</span>
            <button disabled={page >= data.totalPages - 1} onClick={() => setPage(p => p + 1)}>다음</button>
          </div>
        </>
      )}
    </div>
  );
}
