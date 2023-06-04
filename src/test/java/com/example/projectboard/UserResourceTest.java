package com.example.projectboard;

import com.example.projectboard.api.UserResource;
import com.example.projectboard.domain.User;
import com.example.projectboard.dto.UserDto;
import com.example.projectboard.service.UserService;
import com.example.projectboard.vo.user.UserListResponse;
import com.example.projectboard.vo.user.UserRequest;
import com.example.projectboard.vo.user.UserResponse;
import com.example.projectboard.vo.user.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserResourceTest {

    @Mock
    private UserService userService;

    private UserResource userResource;


    @Mock
    private User loginUser;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userResource = new UserResource(userService);
    }

    @Test
    void 유저생성() {

        // Given
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setUsername("testuser");

        UserDto userDto = new UserDto();
        userDto.setUserId("1");
        userDto.setEmail("test@example.com");
        userDto.setUsername("testuser");

        when(userService.save(any(UserRequest.class))).thenReturn(userDto);

        // When
        ResponseEntity<?> responseEntity = userResource.createUser(userRequest);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        MappingJacksonValue jacksonValue = (MappingJacksonValue) responseEntity.getBody();
        UserResponse response = (UserResponse) jacksonValue.getValue();
        assertEquals("1", response.getUserId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("testuser", response.getUsername());
    }

    @Test
    public void 유저조회() {
        String userId = "1";
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setEmail("test@example.com");
        userDto.setUsername("testuser");

        when(userService.findOne(anyString())).thenReturn(userDto);

        // When
        ResponseEntity<?> responseEntity = userResource.retrieveUser(userId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        MappingJacksonValue jacksonValue = (MappingJacksonValue) responseEntity.getBody();
        EntityModel<UserResponse> entityModel = (EntityModel<UserResponse>) jacksonValue.getValue();
        UserResponse response = entityModel.getContent();
        assertEquals(userId, response.getUserId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("testuser", response.getUsername());
    }

    @Test
    public void 전체유저조회() {

        // Given
        String userId1 = "1";
        String userId2 = "2";

        UserDto user1 = new UserDto();
        user1.setEmail("one@example.com");
        user1.setUserId(userId1);
        user1.setUsername("user1");

        UserDto user2 = new UserDto();
        user2.setEmail("one@example.com");
        user2.setUserId(userId2);
        user2.setUsername("user2");

        List<UserDto> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        // When
        when(userService.findAll()).thenReturn(users);

        // Then
        ResponseEntity<?> responseEntity = userResource.retrieveAllUsers();

        assertEquals(responseEntity.getStatusCode(),HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        MappingJacksonValue value = (MappingJacksonValue) responseEntity.getBody();
        UserListResponse userListResponse = (UserListResponse) value.getValue();
        assertEquals(userListResponse.getUserResponses().size(), 2);
        assertEquals(userListResponse.getUserResponses().get(0).getUserId(),userId1);
        assertEquals(userListResponse.getUserResponses().get(1).getUserId(),userId2);
    }

    @Test
    public void 유저수정() {

        // Given

        String userId = "1";
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setEmail("test@example.com");
        userDto.setUsername("username");

        UserUpdateRequest update = new UserUpdateRequest();
        update.setUsername("changeUsername");

        UserDto changed = new UserDto();
        changed.setUserId(userId);
        changed.setEmail("test@example.com");
        changed.setUsername("changeUsername");

        loginUser = User.builder().userId(userId).email("test@example.com").username("username").build();


        // When
        when(userService.updateUserInfo(userId, update, userId)).thenReturn(changed);

        // Then
        ResponseEntity<?> responseEntity = userResource.updateUser(userId, update, loginUser);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.ACCEPTED);
        MappingJacksonValue jacksonValue = (MappingJacksonValue) responseEntity.getBody();
        UserResponse userResponse = (UserResponse) jacksonValue.getValue();

        assertEquals(userResponse.getUsername(),"changeUsername");
    }
}

