package com.example.projectboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 응답")
public class LoginResponse {
    @Schema(description = "토큰 값", defaultValue = "Jwt.token")
    private String access;
    @Schema(description = "아이디", defaultValue = "testId")
    private String userId;
    @Schema(description = "유저네임", defaultValue = "username")
    private String username;
    @Schema(description = "관리자 권한", defaultValue = "isAuth?")
    private String admin;
}
