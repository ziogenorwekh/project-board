package com.example.projectboard.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.example.projectboard.domain.Comment} entity
 */
@Data
public class CommentDto {
    private String comments;
    private String postId;
}