package com.example.projectboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 요청")
public class LoginRequest {

    @Schema(description = "아이디", defaultValue = "testId")
    @Email(message = "이메일 형식이어야 합니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    @NotNull(message = "빈 값은 허용되지 않습니다.")
    private String email;

    @Schema(description = "사용자 비밀번호", defaultValue  = "testPassword")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @NotNull(message = "빈 값은 허용되지 않습니다.")
    private String password;
}
