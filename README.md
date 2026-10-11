# Board Lab — Spring Boot + React 테스트 게시판

## 포함 기능
- 게시글 목록, 상세, 등록, 수정, 삭제
- 입력값 검증, 404 처리, 삭제 확인, 오류 메시지
- H2 **파일 DB**로 게시글 저장 (서버 재시작해도 유지)
- Classist 실습: 실제 Spring Context + H2/JPA 결과 검증 (3개)
- Mockist 실습: Mockito로 Repository 상호작용 검증 (4개)
- React 렌더링 테스트 (1개)

## 사전 준비
- Java 17 이상 (권장 Java 17)
- Maven 3.9 이상 (`mvn` 명령이 인식되어야 함)
- Node.js 20 이상 + npm

## 1. 백엔드 실행
터미널 1:

```powershell
cd backend
mvn spring-boot:run
```

- API: http://localhost:8080/api/posts
- H2 콘솔: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/boarddb`
  - User: `sa`, Password: (빈 칸)
- H2 데이터 파일은 `backend/data/` 안에 생성됩니다. 테스트 DB는 따로 메모리로 분리했습니다.

## 2. 프론트 실행
터미널 2:

```powershell
cd frontend
npm install
npm run dev
```

브라우저 `http://localhost:5173` 접속. Vite 개발 서버가 `/api` 요청을 8080으로 프록시합니다.

## 3. 테스트 실행

```powershell
cd backend
mvn test
```

IntelliJ에서 `PostServiceClassistTest`, `PostServiceMockistTest`를 각각 실행해도 됩니다.

```powershell
cd frontend
npm test
```

### 스타일 차이
| Classist | Mockist |
| --- | --- |
| 실제 JPA Repository + H2 테스트 DB | `mock(PostRepository.class)` |
| 최종 저장/수정/삭제 결과 확인 | `verify`, `ArgumentCaptor`로 호출 및 전달 값 확인 |
| 여러 계층을 함께 검증하므로 통합 테스트 예제로 작성 | Service의 협력 객체를 분리한 단위 테스트 |

**주의:** Classist=통합 테스트, Mockist=단위 테스트라는 뜻은 아닙니다. 이 프로젝트에서 차이를 선명하게 보여주기 위해 이렇게 구성했습니다.

## API 예시
- `GET /api/posts`
- `GET /api/posts/1`
- `POST /api/posts` / `PUT /api/posts/1` — JSON `{ "title":"안녕하세요", "content":"본문", "author":"테스터" }`
- `DELETE /api/posts/1` — 성공 시 204 No Content

## 학습용 프로젝트 참고
- 로그인/권한/페이지네이션/배포 설정은 포함하지 않았습니다.
- 외부에 공개하기 전 인증·권한·CORS·보안 설정이 필요합니다.
- 백엔드는 Spring Boot 3.3.13, React 19, Vite 6 계열로 버전을 고정해 구성했습니다.
