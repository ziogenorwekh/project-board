package com.example.projectboard.vo.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "사용자 정보 응답")
@JsonFilter(value = "user")
public class UserResponse {

    @Schema(description = "유저 UUID", defaultValue = "UUID")
    private String userId;

    @Schema(description = "이메일", defaultValue = "test@example.com")
    private String email;

    @Schema(description = "유저네임", defaultValue = "testUsername")
    private String username;

    @Schema(description = "자기 소개", defaultValue = "testUsername입니다.")
    private String introduce;

}
