package com.example.projectboard.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.example.projectboard.domain.Post} entity
 */
@Data
public class PostDto {

    private String postId;
    private String title;
    private String content;
    private Date postedBy;
    private List<CommentDto> comments;
    private String username;
    private String userId;
}