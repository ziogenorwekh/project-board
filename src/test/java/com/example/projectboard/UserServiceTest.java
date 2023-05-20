package com.example.projectboard;

import com.example.projectboard.dto.UserDto;
import com.example.projectboard.exception.CustomizedResponseException;
import com.example.projectboard.service.TestUserService;
import com.example.projectboard.vo.user.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class UserServiceTest {

    @Autowired
    private TestUserService userService;

    UserDto master;
    UserDto user;

    @BeforeEach
    public void init() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("master");
        userRequest.setUsername("master");
        userRequest.setPassword("1");
        master = userService.save(userRequest);
        userService.giveAdmin(master.getUserId());


        /* Above Master Under user */

        UserRequest userRequest1 = new UserRequest();
        userRequest1.setEmail("user");
        userRequest1.setUsername("user");
        userRequest1.setPassword("1");
        user = userService.save(userRequest1);

    }

    @AfterEach
    public void delete() {
        userService.deleteAll();
    }

    @Test
    public void 어드민계정의일반계정삭제() {
        userService.delete(user.getUserId(), master.getUserId());
    }

    @Test
    public void 일반계정삭제() {
        userService.delete(user.getUserId(), user.getUserId());
    }

    @Test
    public void 일반계정의어드민계정의계정삭제() {
        Assertions.assertThrows(CustomizedResponseException.class, () -> {
            userService.delete(master.getUserId(), user.getUserId());
        });
    }
}
