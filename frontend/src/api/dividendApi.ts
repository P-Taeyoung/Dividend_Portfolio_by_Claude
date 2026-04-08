import axios from 'axios';
import type { DividendRecord, DividendRecordRequest, PageResponse } from '../types';

const BASE = '/api/dividends';

export const dividendApi = {
  getAll: (page = 0, size = 20) =>
    axios.get<PageResponse<DividendRecord>>(BASE, { params: { page, size } }).then(r => r.data),
  getByStockId: (stockId: number) =>
    axios.get<DividendRecord[]>(`${BASE}/stock/${stockId}`).then(r => r.data),
  getByMonth: (year: number, month: number) =>
    axios.get<DividendRecord[]>(`${BASE}/monthly`, { params: { year, month } }).then(r => r.data),
  create: (data: DividendRecordRequest) =>
    axios.post<DividendRecord>(BASE, data).then(r => r.data),
  delete: (id: number) => axios.delete(`${BASE}/${id}`),
};
