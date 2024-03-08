package com.example.projectboard.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Schema(description = "사용자 복수 응답")
public class UserListResponse {
    @Schema(implementation = UserResponse.class)
    private List<UserResponse> userResponses;

    public UserListResponse(List<UserResponse> userResponses) {
        this.userResponses = userResponses;
    }
}
