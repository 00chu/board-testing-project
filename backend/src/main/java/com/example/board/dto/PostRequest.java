package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank(message = "제목을 입력해주세요.") @Size(max = 100, message = "제목은 100자 이하여야 합니다.") String title,
        @NotBlank(message = "내용을 입력해주세요.") String content,
        @NotBlank(message = "작성자를 입력해주세요.") @Size(max = 30, message = "작성자는 30자 이하여야 합니다.") String author
) {
}
