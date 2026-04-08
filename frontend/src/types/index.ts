export type DividendFrequency = 'MONTHLY' | 'QUARTERLY' | 'SEMI_ANNUAL' | 'ANNUAL';

export const FREQUENCY_LABEL: Record<DividendFrequency, string> = {
  MONTHLY: '월배당',
  QUARTERLY: '분기배당',
  SEMI_ANNUAL: '반기배당',
  ANNUAL: '연배당',
};

export interface Stock {
  id: number;
  ticker: string;
  companyName: string;
  sector?: string;
  shares: number;
  avgPurchasePrice: number;
  currentValue: number;
  currency: string;
  dividendPerShare: number;
  dividendFrequency: DividendFrequency;
  annualDividendIncome: number;
  monthlyDividendIncome: number;
  dividendYield: number;
  exDividendDate?: string;
  paymentDate?: string;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface StockRequest {
  ticker: string;
  companyName: string;
  sector?: string;
  shares: number;
  avgPurchasePrice: number;
  currency: string;
  dividendPerShare: number;
  dividendFrequency: DividendFrequency;
  exDividendDate?: string;
  paymentDate?: string;
  notes?: string;
}

export interface StockSummary {
  totalStocks: number;
  totalPortfolioValue: number;
  totalAnnualDividendIncome: number;
  totalMonthlyDividendIncome: number;
  averageDividendYield: number;
  frequencyBreakdown: Record<string, number>;
  sectorBreakdown: Record<string, number>;
}

export interface DividendRecord {
  id: number;
  stockId: number;
  ticker: string;
  companyName: string;
  receivedDate: string;
  amountPerShare: number;
  sharesHeld: number;
  totalAmount: number;
  currency: string;
  notes?: string;
  createdAt: string;
}

export interface DividendRecordRequest {
  stockId: number;
  receivedDate: string;
  amountPerShare: number;
  sharesHeld: number;
  totalAmount: number;
  currency?: string;
  notes?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
