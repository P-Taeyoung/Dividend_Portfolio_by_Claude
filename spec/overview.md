# 프로젝트 개요 (Overview)

## 목적

배당주 및 ETF 투자자를 위한 포트폴리오 관리 웹 애플리케이션.
보유 종목의 예상 배당 수입을 월 단위로 계산하고, 실제 수령한 배당금 이력을 기록·조회한다.

## 기술 스택

| 구분 | 기술 | 버전 |
|------|------|------|
| 언어 | Java | 21 |
| 백엔드 | Spring Boot | 4.0.5 |
| ORM | Spring Data JPA (Hibernate) | Spring Boot BOM |
| 빌드 | Gradle (Kotlin DSL) | - |
| 프론트엔드 | React + TypeScript | React 19 |
| 번들러 | Vite | 8.x |
| HTTP 클라이언트 | Axios | - |
| 라우터 | React Router DOM | - |
| 데이터베이스 | MySQL | 8.4 |
| 컨테이너 | Docker / Docker Compose | - |

## 실행 포트

| 서버 | 포트 | 비고 |
|------|------|------|
| Spring Boot | 8080 | REST API + 통합 빌드 시 정적 파일 서빙 |
| Vite Dev Server | 5173 | 개발 시 React UI, /api 요청은 8080으로 프록시 |
| MySQL (Docker) | 3307 | 호스트 포트 3307 → 컨테이너 내부 3306 |

## 디렉토리 구조

```
claude_practice/
├── src/                          # Spring Boot 소스
│   └── main/
│       ├── java/claude_practice/claude_practice/
│       │   ├── domain/
│       │   │   ├── stock/        # 종목 도메인
│       │   │   └── dividend/     # 배당 기록 도메인
│       │   └── global/
│       │       ├── config/       # WebMvc 설정
│       │       └── exception/    # 전역 예외 처리
│       └── resources/
│           ├── application.properties
│           └── static/           # React 빌드 결과물 (통합 빌드 시)
├── frontend/                     # React 소스
│   └── src/
│       ├── api/                  # Axios API 클라이언트
│       ├── components/           # UI 컴포넌트
│       ├── pages/                # 라우트별 페이지
│       └── types/                # TypeScript 타입 정의
├── spec/                         # 프로젝트 명세 문서
├── docker-compose.yml
├── build.gradle.kts
└── README.md
```
