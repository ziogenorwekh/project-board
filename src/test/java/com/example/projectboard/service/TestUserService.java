package com.example.projectboard.service;

import com.example.projectboard.domain.Role;
import com.example.projectboard.domain.User;
import com.example.projectboard.dto.UserDto;
import com.example.projectboard.exception.CustomizedResponseException;
import com.example.projectboard.repository.UserRepository;
import com.example.projectboard.vo.user.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestUserService {

    private final UserServiceImpl userService;

    private final UserRepository userRepository;

    @Autowired
    public TestUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Transactional
    public UserDto save(UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @Transactional
    public void delete(String userId, String currentUserId) {
        userService.delete(userId, currentUserId);
    }

    /**
     * test
     * @param userId give admin
     */
    @Transactional
    public void giveAdmin(String userId) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "user not in database."));
        Role role = Role.builder().role("ROLE_ADMIN").build();
        role.addUser(user);
    }

    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
