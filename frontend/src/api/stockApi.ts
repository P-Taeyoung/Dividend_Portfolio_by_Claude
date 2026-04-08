import axios from 'axios';
import type { Stock, StockRequest, StockSummary } from '../types';

const BASE = '/api/stocks';

export const stockApi = {
  getAll: () => axios.get<Stock[]>(BASE).then(r => r.data),
  getById: (id: number) => axios.get<Stock>(`${BASE}/${id}`).then(r => r.data),
  getSummary: () => axios.get<StockSummary>(`${BASE}/summary`).then(r => r.data),
  search: (ticker: string) => axios.get<Stock>(`${BASE}/search`, { params: { ticker } }).then(r => r.data),
  create: (data: StockRequest) => axios.post<Stock>(BASE, data).then(r => r.data),
  update: (id: number, data: StockRequest) => axios.put<Stock>(`${BASE}/${id}`, data).then(r => r.data),
  delete: (id: number) => axios.delete(`${BASE}/${id}`),
};
