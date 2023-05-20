package com.example.projectboard.vo.post;

import com.example.projectboard.vo.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Schema(description = "게시글 복수 응답")
public class PostListResponse {
    @Schema(implementation = PostResponse.class)
    private List<PostResponse> postResponses;

    public PostListResponse(List<PostResponse> postResponses) {
        this.postResponses = postResponses;
    }
}
