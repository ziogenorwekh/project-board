package com.example.projectboard.vo.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "게시글 데이터 요청")
public class PostRequest {

    @Schema(description = "글 제목", defaultValue = "안녕하세요")
    @NotNull(message = "빈 값은 허용되지 않습니다.")
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @Schema(description = "글 내용", defaultValue = "좋은 하루입니다.")
    @NotNull(message = "빈 값은 허용되지 않습니다.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
