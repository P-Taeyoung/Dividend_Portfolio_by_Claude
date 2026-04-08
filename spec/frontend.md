# 프론트엔드 명세 (Frontend)

## 기술 선택

| 항목 | 기술 | 이유 |
|------|------|------|
| UI 프레임워크 | React 19 + TypeScript | 타입 안정성, 컴포넌트 재사용 |
| 번들러 | Vite 8 | 빠른 HMR, 간단한 설정 |
| 라우팅 | React Router DOM | 클라이언트 사이드 라우팅 |
| HTTP | Axios | 간결한 API 클라이언트 |
| 스타일 | CSS Modules | 컴포넌트 스코프 격리, 별도 라이브러리 불필요 |

---

## 디렉토리 구조

```
frontend/src/
├── main.tsx                            # React 진입점
├── App.tsx                             # BrowserRouter + Routes 정의
├── index.css                           # 전역 스타일 (reset, body)
│
├── types/
│   └── index.ts                        # TypeScript 타입 정의 (DTO 매핑)
│
├── api/
│   ├── stockApi.ts                     # /api/stocks Axios 호출
│   └── dividendApi.ts                  # /api/dividends Axios 호출
│
├── components/
│   ├── layout/
│   │   ├── Navbar.tsx                  # 상단 네비게이션 바
│   │   ├── Navbar.module.css
│   │   ├── Layout.tsx                  # 페이지 공통 레이아웃 래퍼
│   │   └── Layout.module.css
│   │
│   ├── dashboard/
│   │   ├── SummaryCards.tsx            # 요약 카드 5개 (월배당, 연배당 등)
│   │   ├── SummaryCards.module.css
│   │   ├── FrequencyBreakdown.tsx      # 배당 주기별 막대 차트
│   │   └── FrequencyBreakdown.module.css
│   │
│   ├── stock/
│   │   ├── StockList.tsx               # 포트폴리오 테이블 (수정/삭제 포함)
│   │   ├── StockList.module.css
│   │   ├── StockForm.tsx               # 종목 추가/수정 공용 폼
│   │   └── StockForm.module.css
│   │
│   └── dividend/
│       ├── DividendHistory.tsx         # 배당 기록 페이지네이션 테이블
│       └── DividendHistory.module.css
│
└── pages/
    ├── DashboardPage.tsx               # 라우트: /
    ├── DashboardPage.module.css
    ├── PortfolioPage.tsx               # 라우트: /portfolio
    ├── PortfolioPage.module.css
    ├── DividendHistoryPage.tsx         # 라우트: /history
    └── DividendHistoryPage.module.css
```

---

## 라우팅

| 경로 | 페이지 | 설명 |
|------|--------|------|
| `/` | DashboardPage | 포트폴리오 전체 요약 |
| `/portfolio` | PortfolioPage | 종목 CRUD |
| `/history` | DividendHistoryPage | 배당 수령 기록 조회 |

React Router `BrowserRouter` 사용. Spring Boot `WebConfig`의 SPA 폴백으로 새로고침 시에도 정상 동작.

---

## 타입 정의 (`types/index.ts`)

```typescript
type DividendFrequency = 'MONTHLY' | 'QUARTERLY' | 'SEMI_ANNUAL' | 'ANNUAL';

interface Stock {
  id, ticker, companyName, sector?,
  shares, avgPurchasePrice, currentValue, currency,
  dividendPerShare, dividendFrequency,
  annualDividendIncome, monthlyDividendIncome, dividendYield,
  exDividendDate?, paymentDate?, notes?, createdAt, updatedAt
}

interface StockRequest { /* POST/PUT 요청용 */ }
interface StockSummary { /* 대시보드 집계 */ }
interface DividendRecord { /* 배당 기록 */ }
interface DividendRecordRequest { /* 배당 기록 생성 요청 */ }
interface PageResponse<T> { content, totalElements, totalPages, number, size }
```

`verbatimModuleSyntax` 활성화로 인해 타입 import는 `import type` 사용 필수.

---

## API 클라이언트

### stockApi.ts

```typescript
stockApi.getAll()           → Stock[]
stockApi.getById(id)        → Stock
stockApi.getSummary()       → StockSummary
stockApi.search(ticker)     → Stock
stockApi.create(data)       → Stock
stockApi.update(id, data)   → Stock
stockApi.delete(id)         → void
```

### dividendApi.ts

```typescript
dividendApi.getAll(page, size)         → PageResponse<DividendRecord>
dividendApi.getByStockId(stockId)      → DividendRecord[]
dividendApi.getByMonth(year, month)    → DividendRecord[]
dividendApi.create(data)               → DividendRecord
dividendApi.delete(id)                 → void
```

---

## 주요 컴포넌트 동작

### StockList
- 전체 종목을 테이블 형태로 표시
- 행별 `수정` 클릭 시 해당 행이 인라인 폼(`StockForm`)으로 전환
- 행별 `삭제` 클릭 시 `confirm()` 확인 후 DELETE 요청 → 목록 갱신

### StockForm
- 종목 추가(`PortfolioPage`)와 종목 수정(`StockList` 인라인) 모두 재사용
- `initial` prop으로 초기값 주입 (미입력 시 기본값)
- submit 시 `shares`, `avgPurchasePrice`, `dividendPerShare`를 `Number()`로 변환

### DividendHistory
- 컴포넌트 자체에서 `useEffect`로 데이터 로드 (상태 관리: `useState`)
- 페이지 이동 시 `setPage` → `useEffect` 재실행 → 새 데이터 로드

---

## vite.config.ts 핵심 설정

```typescript
server: {
  proxy: {
    '/api': { target: 'http://localhost:8080', changeOrigin: true }
  }
},
build: {
  outDir: '../src/main/resources/static',  // Spring Boot 정적 파일 폴더로 직접 출력
  emptyOutDir: true,
}
```

---

## 개발 vs 통합 빌드

| 구분 | 실행 방법 | 특징 |
|------|----------|------|
| **개발** | `npm run dev` (포트 5173) | HMR, Vite 프록시로 API 호출 |
| **통합 빌드** | `npm run build` → Gradle 빌드 | Spring Boot JAR에 React 포함, 포트 8080에서 단독 서빙 |
