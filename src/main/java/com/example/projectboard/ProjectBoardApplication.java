package com.example.projectboard;

import com.example.projectboard.dto.UserDto;
import com.example.projectboard.service.PostService;
import com.example.projectboard.service.UserService;
import com.example.projectboard.service.UserServiceImpl;
import com.example.projectboard.vo.user.UserRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectBoardApplication.class, args);
    }


}
