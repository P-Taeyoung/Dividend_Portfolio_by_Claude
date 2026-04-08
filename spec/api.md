# API 명세 (REST API)

Base URL: `http://localhost:8080`

모든 요청/응답의 Content-Type은 `application/json`.

---

## 종목 API `/api/stocks`

### GET `/api/stocks` — 전체 종목 목록

**Response 200**
```json
[
  {
    "id": 1,
    "ticker": "SCHD",
    "companyName": "Schwab US Dividend Equity ETF",
    "sector": "ETF",
    "shares": 100.0000,
    "avgPurchasePrice": 26.5000,
    "currentValue": 2650.0000,
    "currency": "USD",
    "dividendPerShare": 0.2500,
    "dividendFrequency": "QUARTERLY",
    "annualDividendIncome": 100.0000,
    "monthlyDividendIncome": 8.3333,
    "dividendYield": 3.77,
    "exDividendDate": "2025-03-21",
    "paymentDate": "2025-03-25",
    "notes": null,
    "createdAt": "2025-04-01T10:00:00",
    "updatedAt": "2025-04-01T10:00:00"
  }
]
```

---

### GET `/api/stocks/{id}` — 단건 조회

**Response 200** — 위 단일 객체 형식

**Response 404**
```json
{ "message": "종목을 찾을 수 없습니다: id=99", "timestamp": "2025-04-01T10:00:00" }
```

---

### GET `/api/stocks/summary` — 대시보드 집계

**Response 200**
```json
{
  "totalStocks": 3,
  "totalPortfolioValue": 8500.0000,
  "totalAnnualDividendIncome": 380.0000,
  "totalMonthlyDividendIncome": 31.6667,
  "averageDividendYield": 4.47,
  "frequencyBreakdown": {
    "MONTHLY": 200.0000,
    "QUARTERLY": 180.0000
  },
  "sectorBreakdown": {
    "ETF": 5200.0000,
    "금융": 3300.0000
  }
}
```

---

### GET `/api/stocks/search?ticker={ticker}` — 티커 검색

**Query Parameter**: `ticker` (대소문자 무관)

**Response 200** — 단일 Stock 객체

**Response 404** — 종목 없음

---

### POST `/api/stocks` — 종목 추가

**Request Body**
```json
{
  "ticker": "SCHD",
  "companyName": "Schwab US Dividend Equity ETF",
  "sector": "ETF",
  "shares": 100,
  "avgPurchasePrice": 26.5,
  "currency": "USD",
  "dividendPerShare": 0.25,
  "dividendFrequency": "QUARTERLY",
  "exDividendDate": "2025-03-21",
  "paymentDate": "2025-03-25",
  "notes": "핵심 배당 ETF"
}
```

| 필드 | 필수 | 설명 |
|------|------|------|
| ticker | ✅ | 종목 코드 (자동 대문자 변환) |
| companyName | ✅ | 회사명 |
| sector | - | 섹터 |
| shares | ✅ | 양수, 소수점 가능 |
| avgPurchasePrice | ✅ | 양수 |
| currency | ✅ | USD 또는 KRW |
| dividendPerShare | ✅ | 0 이상 |
| dividendFrequency | ✅ | MONTHLY / QUARTERLY / SEMI_ANNUAL / ANNUAL |
| exDividendDate | - | ISO 날짜 (yyyy-MM-dd) |
| paymentDate | - | ISO 날짜 (yyyy-MM-dd) |
| notes | - | 자유 텍스트 |

**Response 201** — 생성된 Stock 객체

**Response 400** — 유효성 검사 실패
```json
{ "message": "ticker: 공백일 수 없습니다, shares: 0보다 커야 합니다" }
```

---

### PUT `/api/stocks/{id}` — 종목 수정

**Request Body** — POST와 동일한 형식

**Response 200** — 수정된 Stock 객체

**Response 404** — 종목 없음

---

### DELETE `/api/stocks/{id}` — 종목 삭제

연관된 `dividend_records`도 CASCADE 삭제된다.

**Response 204** — No Content

**Response 404** — 종목 없음

---

## 배당 기록 API `/api/dividends`

### GET `/api/dividends` — 전체 기록 (페이지네이션)

**Query Parameters**

| 파라미터 | 기본값 | 설명 |
|---------|--------|------|
| page | 0 | 페이지 번호 (0부터 시작) |
| size | 20 | 페이지 크기 |

**Response 200**
```json
{
  "content": [
    {
      "id": 1,
      "stockId": 1,
      "ticker": "SCHD",
      "companyName": "Schwab US Dividend Equity ETF",
      "receivedDate": "2025-03-25",
      "amountPerShare": 0.2487,
      "sharesHeld": 100.0000,
      "totalAmount": 24.8700,
      "currency": "USD",
      "notes": null,
      "createdAt": "2025-04-01T10:00:00"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "size": 20
}
```

---

### GET `/api/dividends/stock/{stockId}` — 종목별 기록

수령일 내림차순 전체 반환 (페이지네이션 없음).

**Response 200** — DividendRecord 배열

---

### GET `/api/dividends/monthly?year={year}&month={month}` — 월별 조회

**Query Parameters**: `year` (연도), `month` (1~12)

**Response 200** — DividendRecord 배열

---

### POST `/api/dividends` — 배당 수령 기록 추가

**Request Body**
```json
{
  "stockId": 1,
  "receivedDate": "2025-03-25",
  "amountPerShare": 0.2487,
  "sharesHeld": 100,
  "totalAmount": 24.87,
  "currency": "USD",
  "notes": "2025년 1분기 배당"
}
```

| 필드 | 필수 | 설명 |
|------|------|------|
| stockId | ✅ | 존재하는 종목 id |
| receivedDate | ✅ | 실제 수령일 |
| amountPerShare | ✅ | 양수 |
| sharesHeld | ✅ | 양수 |
| totalAmount | ✅ | 양수 |
| currency | - | 미입력 시 종목의 currency 사용 |
| notes | - | 자유 텍스트 |

**Response 201** — 생성된 DividendRecord 객체

---

### DELETE `/api/dividends/{id}` — 배당 기록 삭제

**Response 204** — No Content

**Response 404** — 기록 없음

---

## 공통 에러 응답

| HTTP 상태 | 상황 |
|-----------|------|
| 400 | 요청 바디 유효성 검사 실패 |
| 404 | 존재하지 않는 리소스 |
| 500 | 서버 내부 오류 |

```json
{
  "message": "에러 메시지",
  "timestamp": "2025-04-01T10:00:00"
}
```
