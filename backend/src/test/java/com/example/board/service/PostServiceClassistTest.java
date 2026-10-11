package com.example.board.service;

import com.example.board.dto.PostRequest;
import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

// 영속성 컨텍스트: JPA가 엔티티 객체를 관리하는 공간
// Hibernate는 조회한 Post 엔티티를 영속성 컨텍스트에서 관리

// flush() — DB에 변경 사항 반영 요청
// clear() — 영속성 컨텍스트 비우기
// DB에 SQL 실행된 결과까지 확인하기 위해 사용 가능

@SpringBootTest // Spring 컨테이너 실행, 테스트 환경 구성
@ActiveProfiles("test") // application-test.yml 설정 활성화
@Transactional // 테스트를 트랜잭션으로 실행, 테스트 종료 시 롤백

// 최종적으로 해당 동작이 수행이 제대로 되었는지 검증
// 상태 및 결과 중심으로 검증함
class PostServiceClassistTest {
    @Autowired
    PostService service;
    @Autowired
    PostRepository repository;

    @Test
    void shouldPersistPostInDatabase() {
        // Given: 등록할 게시글 임시 데이터
        PostRequest request =
                new PostRequest("제목", "내용", "1234");

        // When: 게시글 등록
        var result = service.create(request);

        // Then: 등록한 게시글 DB에서 조회
        Post found = repository.findById(result.id())
                .orElseThrow(); // 데이터 없으면 오류 발생

        // 조회한 데이터 검증 (기대값과 같은지)
        // AssertJ에서 제공하는 검증 기능
        // 실제 값과 같으면 테스트 통과
        assertThat(found.getTitle()).isEqualTo("제목");
        assertThat(found.getContent()).isEqualTo("내용");
    }

    @Test
    void shouldUpdatePostInDatabase() {
        // Given: 수정할 게시글 생성
        // 테스트가 실행될 때마다 필요한 데이터 준비하여 테스트
        Long id = service.create(
                new PostRequest("원래 제목", "원래 내용", "1234")
        ).id();

        // When: 게시글 수정
        service.update(
                id,
                new PostRequest("바뀐 제목", "새 내용", "5678")
        );

        // Then: 변경된 값 검증
        Post found = repository.findById(id)
                .orElseThrow();

        // 검증
        assertThat(found.getTitle()).isEqualTo("바뀐 제목");
        assertThat(found.getContent()).isEqualTo("새 내용");
    }

    @Test
    void shouldDeletePostFromDatabase() {
        // Given: 삭제할 게시글 생성
        Long id = service.create(
                new PostRequest("삭제", "본문", "1234")
        ).id();

        // When: 게시글 삭제
        service.delete(id);

        // Then: 게시글이 삭제됐는지 확인
        boolean exists = repository.existsById(id);

        // 게시글이 없을 때 false가 반환되므로 검증
        assertThat(exists).isFalse();
    }
}
