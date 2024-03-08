package com.example.projectboard.service;

import com.example.projectboard.dto.UserDto;
import com.example.projectboard.vo.user.UserRequest;
import com.example.projectboard.vo.user.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserDto save(UserRequest userRequest);

    UserDto findOne(String userId);

    List<UserDto> findAll();


    UserDto updateUserInfo(String userId, UserUpdateRequest userUpdateRequest, String currentUserId);

    void delete(String userId,String currentUserId);

}
