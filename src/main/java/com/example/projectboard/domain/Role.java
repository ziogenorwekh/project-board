package com.example.projectboard.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity(name = "user_role")
@Getter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Role(String role) {
        this.role = role;
    }

    public void addUser(User user) {
        this.user = user;
        user.getRoles().add(this);
    }
}
