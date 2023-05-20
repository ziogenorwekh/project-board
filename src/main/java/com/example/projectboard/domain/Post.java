package com.example.projectboard.domain;

import com.example.projectboard.vo.post.PostRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @Column(name = "post_uuid", nullable = false, unique = true)
    private String postId;

    private String title;

    private String content;

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date postedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String title, String content) {
        this.postId = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
    }

    public static Post createPost(PostRequest postRequest, User user) {
        Post post = Post.builder().title(postRequest.getTitle()).content(postRequest.getContent())
                .build();
        post.addUser(user);
        return post;
    }

    public void addUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }

    public void update(PostRequest postRequest) {
        this.title = postRequest.getTitle();
        this.content = postRequest.getContent();
    }

    public void delete() {
        this.user.getPosts().remove(this);
        this.user = null;
        this.comments.clear();
    }

}
