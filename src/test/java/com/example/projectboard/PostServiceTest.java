package com.example.projectboard;

import com.example.projectboard.dto.PostDto;
import com.example.projectboard.dto.UserDto;
import com.example.projectboard.exception.CustomizedResponseException;
import com.example.projectboard.service.TestPostService;
import com.example.projectboard.service.TestUserService;
import com.example.projectboard.vo.post.PostRequest;
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
@SuppressWarnings("NonAsciiCharacters")
public class PostServiceTest {

    @Autowired
    private TestUserService userService;
    @Autowired
    private TestPostService postService;

    UserDto master;
    UserDto user;

    UserDto otherUser;

    PostDto masterPost;
    PostDto userPost;

    PostDto otherUserPost;

    @BeforeEach
    public void init() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("master");
        userRequest.setUsername("master");
        userRequest.setPassword("1");
        master = userService.save(userRequest);
        userService.giveAdmin(master.getUserId());

        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("adminTitle");
        postRequest.setContent("adminContent");
        masterPost = postService.save(postRequest, master.getUserId());

        /* Above Master Under user */

        UserRequest userRequest1 = new UserRequest();
        userRequest1.setEmail("user");
        userRequest1.setUsername("user");
        userRequest1.setPassword("1");
        user = userService.save(userRequest1);

        PostRequest postRequest1 = new PostRequest();
        postRequest1.setTitle("userTitle");
        postRequest1.setContent("userContent");
        userPost = postService.save(postRequest1, user.getUserId());

        UserRequest userRequest2 = new UserRequest();
        userRequest2.setEmail("otherUser");
        userRequest2.setUsername("otherUser");
        userRequest2.setPassword("1");
        otherUser = userService.save(userRequest2);

        PostRequest postRequest2 = new PostRequest();
        postRequest2.setTitle("otherUserTitle");
        postRequest2.setContent("otherUserContent");
        otherUserPost = postService.save(postRequest2, otherUser.getUserId());
    }

    @AfterEach
    public void delete() {
        userService.deleteAll();
        postService.deleteAll();
    }


    @Test
    public void 어드민계정의일반유저의게시글삭제() {
        postService.delete(userPost.getPostId(), master.getUserId());
    }

    @Test
    public void 일반계정의게시글삭제() {
        postService.delete(userPost.getPostId(), user.getUserId());
    }

    @Test
    public void 일반계정의다른일반계정게시글삭제() {
        Assertions.assertThrows(CustomizedResponseException.class, () ->
                postService.delete(otherUserPost.getPostId(), user.getUserId()));
    }

    @Test
    public void 일반계정이어드민계정의게시글삭제() {
        Assertions.assertThrows(CustomizedResponseException.class, () ->
                postService.delete(masterPost.getPostId(), user.getUserId()));
    }
}
