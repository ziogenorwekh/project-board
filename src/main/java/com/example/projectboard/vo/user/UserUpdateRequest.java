package com.example.projectboard.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 수정 요청")
public class UserUpdateRequest {

    @Schema(description = "유저네임", defaultValue = "testUsername")
    @NotBlank(message = "빈 값은 허용되지 않습니다.")
    @Size(min = 3, message = "최소 세글자 이상의 아이디를 입력해주세요.")
    @NotNull(message = "빈 값은 허용되지 않습니다.")
    private String username;

    @Schema(description = "비밀번호", defaultValue = "testPassword")
    @Size(min = 4, message = "최소 네글자 이상의 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z\\\\d`~!@#$%^&*()-_=+]{4,16}$", message = "특수 문자는 적어도 하나 이상 포함되어야 합니다.")
    @NotNull(message = "빈 값은 허용되지 않습니다.")
    private String password;


    private String introduce;
}
