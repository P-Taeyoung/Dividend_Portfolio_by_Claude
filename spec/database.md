# 데이터베이스 명세 (Database)

## 환경

- **DBMS**: MySQL 8.4 (Docker 컨테이너)
- **데이터베이스명**: `dividend_db`
- **문자셋**: `utf8mb4` / `utf8mb4_unicode_ci`
- **타임존**: `Asia/Seoul`
- **접속 정보**
  - Host: `localhost:3307`
  - User: `dividend_user`
  - Password: `dividend1234`

---

## 테이블: `stocks`

보유 종목 포트폴리오. 한 행이 하나의 종목(포지션)을 나타낸다.

| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| `id` | BIGINT | PK, AUTO_INCREMENT | 식별자 |
| `ticker` | VARCHAR(20) | NOT NULL, UNIQUE | 종목 코드 (대문자 저장) |
| `company_name` | VARCHAR(200) | NOT NULL | 회사명 / ETF명 |
| `sector` | VARCHAR(100) | NULL | 섹터 (ETF, 금융, IT 등) |
| `shares` | DECIMAL(12,4) | NOT NULL | 보유 주수 (소수점 지원) |
| `avg_purchase_price` | DECIMAL(12,4) | NOT NULL | 평균 매입가 |
| `currency` | VARCHAR(3) | NOT NULL, DEFAULT 'USD' | 통화 코드 (USD, KRW) |
| `dividend_per_share` | DECIMAL(10,4) | NOT NULL | 주당 배당금 |
| `dividend_frequency` | ENUM | NOT NULL | 배당 주기 |
| `ex_dividend_date` | DATE | NULL | 배당락일 |
| `payment_date` | DATE | NULL | 배당 지급일 |
| `notes` | TEXT | NULL | 메모 |
| `created_at` | DATETIME | NOT NULL | 생성 시각 (자동) |
| `updated_at` | DATETIME | NOT NULL | 수정 시각 (자동) |

### `dividend_frequency` ENUM 값

| 값 | 설명 | 연간 지급 횟수 |
|----|------|--------------|
| `MONTHLY` | 월배당 | 12 |
| `QUARTERLY` | 분기배당 | 4 |
| `SEMI_ANNUAL` | 반기배당 | 2 |
| `ANNUAL` | 연배당 | 1 |

### 계산 필드 (DB 미저장, 서비스 레이어에서 계산)

| 필드 | 계산식 |
|------|--------|
| 연간 배당 수입 | `dividend_per_share × shares × paymentsPerYear` |
| 월간 배당 수입 | `연간 배당 수입 ÷ 12` |
| 포트폴리오 가치 | `avg_purchase_price × shares` |
| 배당수익률 | `연간 배당 수입 ÷ 포트폴리오 가치 × 100` |

---

## 테이블: `dividend_records`

실제로 수령한 배당금 이력. `stocks`와 N:1 관계.

| 컬럼명 | 타입 | 제약 | 설명 |
|--------|------|------|------|
| `id` | BIGINT | PK, AUTO_INCREMENT | 식별자 |
| `stock_id` | BIGINT | NOT NULL, FK → stocks.id | 종목 참조 |
| `received_date` | DATE | NOT NULL | 실제 수령일 |
| `amount_per_share` | DECIMAL(10,4) | NOT NULL | 수령 시점 주당 배당금 |
| `shares_held` | DECIMAL(12,4) | NOT NULL | 수령 시점 보유 주수 |
| `total_amount` | DECIMAL(14,4) | NOT NULL | 총 수령 배당금 |
| `currency` | VARCHAR(3) | NOT NULL | 통화 코드 |
| `notes` | TEXT | NULL | 메모 |
| `created_at` | DATETIME | NOT NULL | 생성 시각 (자동) |

### 외래키 제약

```sql
CONSTRAINT fk_dividend_stock
    FOREIGN KEY (stock_id) REFERENCES stocks(id)
    ON DELETE CASCADE
```

종목 삭제 시 해당 종목의 배당 기록도 함께 삭제된다.

---

## ERD

```
stocks                          dividend_records
─────────────────────           ────────────────────────
id            PK                id              PK
ticker        UNIQUE             stock_id        FK → stocks.id
company_name                    received_date
sector                          amount_per_share
shares                          shares_held
avg_purchase_price              total_amount
currency                        currency
dividend_per_share              notes
dividend_frequency              created_at
ex_dividend_date
payment_date
notes
created_at
updated_at
```

---

## DDL (자동 생성)

테이블은 `spring.jpa.hibernate.ddl-auto=update` 설정에 의해 애플리케이션 첫 실행 시 Hibernate가 자동 생성한다. 직접 DDL을 실행할 필요 없다.
