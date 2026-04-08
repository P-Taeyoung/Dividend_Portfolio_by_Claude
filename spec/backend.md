# 백엔드 명세 (Backend)

## 패키지 구조

```
claude_practice.claude_practice
├── ClaudePracticeApplication.java          # 진입점 (@SpringBootApplication)
├── domain/
│   ├── stock/                              # 종목 도메인
│   │   ├── Stock.java                      # JPA 엔티티
│   │   ├── DividendFrequency.java          # 배당 주기 Enum
│   │   ├── StockRepository.java            # Spring Data JPA 인터페이스
│   │   ├── StockService.java               # 서비스 인터페이스
│   │   ├── StockServiceImpl.java           # 서비스 구현체
│   │   ├── StockController.java            # REST 컨트롤러 (/api/stocks)
│   │   └── dto/
│   │       ├── StockRequestDto.java        # 종목 생성/수정 요청
│   │       ├── StockResponseDto.java       # 종목 응답 (계산 필드 포함)
│   │       └── StockSummaryDto.java        # 대시보드 집계 응답
│   └── dividend/                           # 배당 기록 도메인
│       ├── DividendRecord.java             # JPA 엔티티
│       ├── DividendRecordRepository.java
│       ├── DividendRecordService.java
│       ├── DividendRecordServiceImpl.java
│       ├── DividendRecordController.java   # REST 컨트롤러 (/api/dividends)
│       └── dto/
│           ├── DividendRecordRequestDto.java
│           └── DividendRecordResponseDto.java
└── global/
    ├── config/
    │   └── WebConfig.java                  # SPA 폴백 라우팅
    └── exception/
        ├── ResourceNotFoundException.java  # 404 예외
        ├── ErrorResponse.java              # 에러 응답 DTO
        └── GlobalExceptionHandler.java     # @RestControllerAdvice
```

---

## 도메인 상세

### Stock (종목)

**엔티티 주요 사항**
- `ticker`는 저장 시 항상 대문자로 변환 (서비스 레이어)
- `currency` 기본값: `"USD"` (`@Builder.Default`)
- `annualDividendIncome()` 편의 메서드: `dividendPerShare × shares × paymentsPerYear`
- 타임스탬프: `@CreationTimestamp` / `@UpdateTimestamp` (Hibernate 자동 관리)

**StockResponseDto 계산 필드**

| 필드 | 계산식 |
|------|--------|
| `currentValue` | `avgPurchasePrice × shares` |
| `annualDividendIncome` | `dividendPerShare × shares × paymentsPerYear` |
| `monthlyDividendIncome` | `annualDividendIncome ÷ 12` |
| `dividendYield` | `annualDividendIncome ÷ currentValue × 100` (소수점 2자리) |

**StockSummaryDto 집계 로직** (StockServiceImpl.getSummary)
- 전체 `stocks` 조회 후 Java Stream으로 집계 (DB 집계 함수 미사용)
- `frequencyBreakdown`: 배당 주기별 연간 배당 수입 합계
- `sectorBreakdown`: 섹터별 포트폴리오 가치 합계 (sector가 null이거나 빈 값인 종목 제외)

**StockRepository 커스텀 쿼리**
```java
Optional<Stock> findByTickerIgnoreCase(String ticker);
boolean existsByTickerIgnoreCase(String ticker);
```

---

### DividendRecord (배당 기록)

**엔티티 주요 사항**
- `stock`과 `@ManyToOne(fetch = FetchType.LAZY)` 관계
- `totalAmount`는 저장 값 (수령 시점 기준, 이후 `shares` 변경에도 기록 유지)
- `currency` 미입력 시 연관 Stock의 currency 사용 (서비스 레이어)

**DividendRecordRepository 커스텀 쿼리**
```java
List<DividendRecord> findByStockIdOrderByReceivedDateDesc(Long stockId);

@Query("SELECT d FROM DividendRecord d WHERE YEAR(d.receivedDate) = :year AND MONTH(d.receivedDate) = :month ORDER BY d.receivedDate DESC")
List<DividendRecord> findByYearAndMonth(int year, int month);

Page<DividendRecord> findAllByOrderByReceivedDateDesc(Pageable pageable);
```

---

## 전역 설정

### WebConfig (SPA 폴백)

`/api/**` 이외의 모든 경로 요청 시 `classpath:/static/index.html`을 반환한다.
React Router의 클라이언트 사이드 라우팅(`/portfolio`, `/history` 등)이 새로고침 후에도 동작하도록 보장한다.

### GlobalExceptionHandler

| 예외 | HTTP 상태 |
|------|-----------|
| `ResourceNotFoundException` | 404 Not Found |
| `MethodArgumentNotValidException` | 400 Bad Request |
| `Exception` (기타) | 500 Internal Server Error |

---

## application.properties 설정값

| 키 | 값 | 비고 |
|----|-----|------|
| `server.port` | 8080 | |
| `spring.datasource.url` | `jdbc:mysql://localhost:3307/dividend_db` | Docker 포트 3307 |
| `spring.datasource.username` | `dividend_user` | |
| `spring.jpa.hibernate.ddl-auto` | `update` | 개발용, 운영 시 `validate`로 변경 |
| `spring.jpa.show-sql` | `true` | 개발 편의용 |
| `spring.web.resources.static-locations` | `classpath:/static/` | React 빌드 결과물 위치 |

---

## 의존성 (build.gradle.kts)

```kotlin
implementation("org.springframework.boot:spring-boot-starter-web")
implementation("org.springframework.boot:spring-boot-starter-data-jpa")
implementation("org.springframework.boot:spring-boot-starter-validation")
runtimeOnly("com.mysql:mysql-connector-j")
compileOnly("org.projectlombok:lombok")
annotationProcessor("org.projectlombok:lombok")
testImplementation("org.springframework.boot:spring-boot-starter-test")
```
