package com.example.projectboard.vo.post;

import com.example.projectboard.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Schema(description = "게시글 데이터 응답")
@Setter
@Getter
@JsonFilter("post")
public class PostResponse {

    @Schema(description = "글 UUID", defaultValue = "UUID")
    private String postId;
    @Schema(description = "글 제목", defaultValue = "안녕하세요")
    private String title;
    @Schema(description = "글 내용", defaultValue = "좋은 하루입니다.")
    private String content;
    @Schema(description = "최초 작성일")
    @JsonFormat(pattern = "YY-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date postedBy;

    @Schema(description = "게시글의 댓글")
    private List<String> comments;

    @Schema(description = "작성자")
    private String username;

    @Schema(description = "작성자 id")
    private String userId;

    public PostResponse(PostDto postDto) {
        this.postId = postDto.getPostId();
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.postedBy = postDto.getPostedBy();
        this.comments = new ArrayList<>();
        this.username = postDto.getUsername();
        this.userId = postDto.getUserId();
        postDto.getComments().forEach(commentDto -> this.comments.add(commentDto.getComments()));
    }
}
