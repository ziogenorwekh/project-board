package com.example.projectboard.domain;

import com.example.projectboard.vo.user.UserRequest;
import com.example.projectboard.vo.user.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "user_uuid", nullable = false, unique = true)
    private String userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    private String password;

    private String encryptedPassword;

    private String introduce;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public User(String email, String username, String password, String encryptedPassword,
                String introduce,String userId) {
        this.introduce = introduce;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.encryptedPassword = encryptedPassword;
    }


    // business logic
    public static User createUser(UserRequest userRequest, String encryptedPassword) {

        User user = User.builder().email(userRequest.getEmail()).username(userRequest.getUsername())
                .password(userRequest.getPassword()).encryptedPassword(encryptedPassword)
                .introduce(userRequest.getIntroduce())
                .userId(UUID.randomUUID().toString())
                .build();
        Role role = Role.builder().role("ROLE_USER").build();
        role.addUser(user);
        return user;
    }

    public void updateInfo(UserUpdateRequest userUpdateRequest, String encryptedPassword) {
        this.introduce = userUpdateRequest.getIntroduce();
        this.username = userUpdateRequest.getUsername();
        this.password = userUpdateRequest.getPassword();
        this.encryptedPassword = encryptedPassword;
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getRole().equals(roleName));
    }

    public void delete() {
        this.getRoles().clear();
    }

}
