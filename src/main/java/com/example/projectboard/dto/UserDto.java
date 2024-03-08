package com.example.projectboard.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.example.projectboard.domain.User} entity
 */
@Data
public class UserDto {
    private String userId;
    private String email;
    private String username;
    private String introduce;
}