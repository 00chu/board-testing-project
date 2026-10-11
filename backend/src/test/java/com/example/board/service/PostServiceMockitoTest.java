package com.example.board.service;

import com.example.board.dto.PostRequest;
import com.example.board.entity.Post;
import com.example.board.exception.PostNotFoundException;
import com.example.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// Mockist(모키스트): Mock 객체를 중심으로 상호작용을 검증하는 테스트 스타일
// Mockito(모키토): Java에서 Mock 객체를 생성하고 검증하는 테스트 라이브러리

// 테스트 대상 객체가 다른 객체를 올바르게 호출했는지를 검증
class PostServiceMockitoTest {
    // PostRepository의 Mock 객체 생성. 실제 DB에 연결하지 않음
    private final PostRepository repository = mock(PostRepository.class);
    private final PostService service = new PostService(repository);

    @Test
    void shouldPassCorrectPostToRepository() {

        // Given: Mock 객체가 어떻게 동작할지 설정
        when(repository.saveAndFlush(any(Post.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // When: 테스트할 Service 실행, 실제 DB CRUD는 실행하지 않음
        service.create(
                new PostRequest("제목", "내용", "1234")
        );

        // Then: 호출 여부와 인자 검증
        ArgumentCaptor<Post> captor =
                ArgumentCaptor.forClass(Post.class);

        // verify(): 해당 메서드가 실제로 호출되었는지 검증
        // times(1): 정확히 한 번 호출되었는지 확인
        // captor.capture(): 메서드에 전달된 객체를 저장
        verify(repository, times(1))
                .saveAndFlush(captor.capture());

        assertThat(captor.getValue().getTitle())
                .isEqualTo("제목");

        assertThat(captor.getValue().getContent())
                .isEqualTo("내용");
    }

    @Test
    void shouldSaveUpdatedPostAfterFindingIt() {
        // Given
        Long id = 1L;
        Post post = new Post("원래 제목", "원래 내용", "1234");

        when(repository.findById(id))
                .thenReturn(Optional.of(post)); // 미리 준비한 post 반환

        when(repository.saveAndFlush(any(Post.class))) // Post 타입의 객체가 인자로 들어오면 매칭
                .thenAnswer(inv -> inv.getArgument(0)); // 전달받은 첫 번째 인자를 그대로 반환

        // When
        service.update(
                id,
                new PostRequest("바뀐 제목", "새 내용", "1234")
        );

        // Then
        verify(repository).findById(id);

        verify(repository).saveAndFlush(post);

        assertThat(post.getTitle())
                .isEqualTo("바뀐 제목");
    }

    @Test
    // Repository의 삭제 메서드를 올바르게 호출했는지를 검증
    void shouldCallRepositoryDelete() {
        // Given
        Long id = 1L;

        when(repository.existsById(id))
                .thenReturn(true);

        // When
        service.delete(id);

        // Then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    // 존재하지 않는 게시글을 조회했을 때 예외가 발생하는지
    void shouldThrowWhenPostDoesNotExist() {

        // Given
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // When+Then
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(PostNotFoundException.class);

        // Then
        verify(repository).findById(id);
        verify(repository, never())
                .saveAndFlush(any(Post.class));
    }
}
