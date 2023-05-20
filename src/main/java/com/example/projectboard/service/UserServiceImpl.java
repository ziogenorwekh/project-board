package com.example.projectboard.service;

import com.example.projectboard.domain.User;
import com.example.projectboard.dto.UserDto;
import com.example.projectboard.exception.CustomizedResponseException;
import com.example.projectboard.repository.UserRepository;
import com.example.projectboard.vo.user.UserRequest;
import com.example.projectboard.vo.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public UserDto save(UserRequest userRequest) {
        checkEmailDuplicate(userRequest.getEmail());
        String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());

        User user = User.createUser(userRequest, encryptedPassword);
        userRepository.save(user);

        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findOne(String userId) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "user not in database.")
        );
        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new ModelMapper().map(user, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUserInfo(String userId, UserUpdateRequest userUpdateRequest, String currentUserId) {

        if (!userId.equals(currentUserId)) {
            throw new CustomizedResponseException(HttpStatus.UNAUTHORIZED, "login user not matched current user.");
        }

        User user = userRepository.findUserByUserId(userId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "user not in database."));

        String encryptedPassword = passwordEncoder.encode(userUpdateRequest.getPassword());

        user.updateInfo(userUpdateRequest, encryptedPassword);

        return new ModelMapper().map(user, UserDto.class);
    }


    /**
     * @param userId        will delete user
     * @param currentUserId current login user
     */
    @Override
    @Transactional
    public void delete(String userId, String currentUserId) {


        User userToDelete = userRepository.findUserByUserId(userId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "User not found."));

        User currentUser = userRepository.findUserByUserId(currentUserId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete the user."));

        if (currentUser.getUserId().equals(userId) || currentUser.hasRole("ROLE_ADMIN")) {
            userToDelete.delete();
            userRepository.delete(userToDelete);
        } else {
            throw new CustomizedResponseException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete the user.");
        }
    }


    private void checkEmailDuplicate(String email) {
        userRepository.findUserByEmail(email).ifPresent(user -> {
            throw new CustomizedResponseException(HttpStatus.CONFLICT,
                    String.format("email %s already exist.", user.getEmail()));
        });
    }


}
