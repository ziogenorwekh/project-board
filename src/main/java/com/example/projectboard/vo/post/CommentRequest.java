package com.example.projectboard.vo.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "댓글 작성 요청")
public class CommentRequest {

    @Schema(description = "댓글", defaultValue = "test comments.")
    @Size(min = 2, message = "최소 두 글자 이상 댓글을 적어주시길 바랍니다.")
    @NotNull(message = "빈 값은 허용되지 않습니다.")
    private String comment;
}
