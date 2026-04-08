# 월배당 포트폴리오 관리 앱

배당주/ETF 포트폴리오를 관리하고 월별 배당 수입을 추적하는 웹 애플리케이션입니다.

## 기술 스택

- **백엔드**: Spring Boot 4.0.5, Java 21, Spring Data JPA
- **프론트엔드**: React 19, TypeScript, Vite
- **데이터베이스**: MySQL 8.4 (Docker 컨테이너)

---

## 사전 요구사항

- Java 21
- Node.js 18+
- Docker Desktop

---

## 시작하기

### 1. 데이터베이스 실행 (Docker)

```bash
docker compose up -d
```

MySQL 컨테이너가 시작됩니다. `dividend_db` 데이터베이스와 사용자가 자동으로 생성됩니다.

| 항목 | 값 |
|------|----|
| 호스트 | localhost:3307 |
| 데이터베이스 | dividend_db |
| 사용자 | dividend_user |
| 비밀번호 | dividend1234 |

### 2. 백엔드 실행 (Spring Boot)

```bash
./gradlew bootRun
```

- 주소: http://localhost:8080
- 첫 실행 시 Hibernate가 테이블을 자동 생성합니다.

### 3. 프론트엔드 실행 (React)

```bash
cd frontend
npm install   # 최초 1회만
npm run dev
```

- 주소: http://localhost:5173
- Spring Boot API로의 요청은 자동으로 프록시됩니다.

> 백엔드와 프론트엔드를 **별도 터미널**에서 각각 실행해야 합니다.

---

## 화면 구성

### 대시보드 (`/`)

포트폴리오 전체 현황을 한눈에 확인합니다.

- **요약 카드**: 보유 종목 수, 월 배당 수입, 연 배당 수입, 포트폴리오 총액, 평균 배당수익률
- **배당 주기별 분포**: 월배당/분기배당/반기배당/연배당 비중 막대 차트
- **섹터별 포트폴리오**: 섹터별 투자 금액 현황

### 포트폴리오 (`/portfolio`)

보유 종목을 관리합니다.

**종목 추가**
1. `+ 종목 추가` 버튼 클릭
2. 아래 정보 입력 후 저장:
   - 티커 (예: SCHD, JEPI, QQQ)
   - 회사명
   - 섹터 (선택)
   - 보유 주수
   - 평균 매입가
   - 통화 (USD / KRW)
   - 주당 배당금
   - 배당 주기 (월배당 / 분기배당 / 반기배당 / 연배당)
   - 배당락일 / 배당 지급일 (선택)

**종목 수정**: 해당 행의 `수정` 버튼 클릭 → 인라인 폼에서 수정 후 저장

**종목 삭제**: 해당 행의 `삭제` 버튼 클릭 → 확인 후 삭제

### 배당 기록 (`/history`)

실제로 수령한 배당금 내역을 확인합니다.

- 수령일, 종목, 주당 배당금, 보유 주수, 총 수령액 조회
- 20건씩 페이지네이션
- 잘못 입력한 기록은 `삭제` 버튼으로 제거

---

## REST API

### 종목 (Stocks)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/api/stocks` | 전체 종목 목록 |
| GET | `/api/stocks/{id}` | 종목 단건 조회 |
| GET | `/api/stocks/summary` | 대시보드 집계 데이터 |
| GET | `/api/stocks/search?ticker=SCHD` | 티커로 종목 검색 |
| POST | `/api/stocks` | 종목 추가 |
| PUT | `/api/stocks/{id}` | 종목 수정 |
| DELETE | `/api/stocks/{id}` | 종목 삭제 |

### 배당 기록 (Dividends)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/api/dividends` | 전체 배당 기록 (페이지네이션) |
| GET | `/api/dividends/stock/{stockId}` | 특정 종목의 배당 기록 |
| GET | `/api/dividends/monthly?year=2025&month=3` | 월별 배당 기록 |
| POST | `/api/dividends` | 배당 수령 기록 추가 |
| DELETE | `/api/dividends/{id}` | 배당 기록 삭제 |

**종목 추가 예시 (curl)**

```bash
curl -X POST http://localhost:8080/api/stocks \
  -H "Content-Type: application/json" \
  -d '{
    "ticker": "SCHD",
    "companyName": "Schwab US Dividend Equity ETF",
    "sector": "ETF",
    "shares": 100,
    "avgPurchasePrice": 26.5,
    "currency": "USD",
    "dividendPerShare": 0.25,
    "dividendFrequency": "QUARTERLY"
  }'
```

---

## 빌드 명령어

```bash
# 백엔드 빌드
./gradlew build

# 백엔드 테스트
./gradlew test

# 프론트엔드 빌드 (결과물 → src/main/resources/static/)
cd frontend && npm run build

# 통합 빌드 (React + Spring Boot 단일 JAR)
./gradlew build
java -jar build/libs/claude_practice-0.0.1-SNAPSHOT.jar
```

통합 빌드 후 실행하면 http://localhost:8080 에서 UI와 API를 모두 제공합니다.

---

## Docker 컨테이너 관리

```bash
# 시작
docker compose up -d

# 상태 확인
docker compose ps

# 로그 확인
docker compose logs -f

# 중지 (데이터 유지)
docker compose stop

# 완전 삭제 (데이터 포함)
docker compose down -v
```
