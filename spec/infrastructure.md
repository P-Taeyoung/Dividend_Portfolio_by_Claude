# 인프라 명세 (Infrastructure)

## Docker Compose

파일 위치: `docker-compose.yml` (프로젝트 루트)

```yaml
services:
  mysql:
    image: mysql:8.4
    container_name: dividend_mysql
    ports:
      - "3307:3306"       # 호스트 3307 → 컨테이너 3306
    volumes:
      - mysql_data:/var/lib/mysql   # 데이터 영속 볼륨

volumes:
  mysql_data:
```

### 포트 선택 이유

기존 `coinwash` 프로젝트 컨테이너가 호스트 3306을 점유하고 있어 3307로 설정.
Spring Boot는 `localhost:3307`로 접속.

### 환경 변수

| 변수 | 값 |
|------|----|
| MYSQL_ROOT_PASSWORD | dividend1234 |
| MYSQL_DATABASE | dividend_db |
| MYSQL_USER | dividend_user |
| MYSQL_PASSWORD | dividend1234 |

### 컨테이너 시작 시 자동 수행 작업

1. `dividend_db` 데이터베이스 생성
2. `dividend_user` 계정 생성
3. `dividend_user`에게 `dividend_db` 전체 권한 부여

### 데이터 볼륨

`claude_practice_mysql_data` 볼륨에 MySQL 데이터 영속 저장.
`docker compose down` 만으로는 데이터가 삭제되지 않음.
데이터까지 초기화하려면 `docker compose down -v` 사용.

---

## Gradle 빌드 통합

`build.gradle.kts`에 React 빌드 자동화 태스크 포함:

```kotlin
val buildFrontend = tasks.register<Exec>("buildFrontend") {
    workingDir(project.file("frontend"))
    commandLine("npm", "run", "build")
}

tasks.named("processResources") {
    dependsOn(buildFrontend)
}
```

`./gradlew build` 실행 시 자동으로:
1. `frontend/` 디렉토리에서 `npm run build` 실행
2. Vite가 `src/main/resources/static/`에 빌드 결과물 출력
3. Gradle이 `static/`을 JAR에 패키징
4. 최종 JAR 하나로 UI + API 모두 서빙 가능

---

## 실행 환경 요약

### 개발 환경

```
[브라우저 :5173] ──→ [Vite Dev Server :5173]
                            │ /api/* 프록시
                            ↓
                     [Spring Boot :8080]
                            │
                            ↓
                     [MySQL Docker :3307]
```

### 운영/통합 환경

```
[브라우저 :8080] ──→ [Spring Boot :8080]
                      ├── /api/*  → REST Controller
                      ├── /*.js   → static/assets/
                      ├── /*.css  → static/assets/
                      └── 그 외   → static/index.html (SPA 폴백)
                            │
                            ↓
                     [MySQL :3307]
```

---

## 관리 명령어

```bash
# 컨테이너 상태 확인
docker compose ps

# 로그 확인
docker compose logs -f mysql

# MySQL 접속
docker exec -it dividend_mysql mysql -u dividend_user -pdividend1234 dividend_db

# 컨테이너 중지 (데이터 보존)
docker compose stop

# 컨테이너 + 네트워크 삭제 (데이터 보존)
docker compose down

# 컨테이너 + 데이터 볼륨 완전 삭제
docker compose down -v
```
