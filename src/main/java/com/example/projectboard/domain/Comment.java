package com.example.projectboard.domain;

import com.example.projectboard.vo.post.CommentRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "post_comment")
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(name = "comment_uuid", nullable = false)
    private String commentId;

    @Column(length = 150)
    private String comments;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(String comments) {
        this.commentId = UUID.randomUUID().toString();
        this.comments = comments;
    }

    public static void createComment(CommentRequest commentRequest, Post post) {
        Comment comment = Comment.builder().comments(commentRequest.getComment())
                .build();
        comment.addPost(post);
    }

    public void addPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }
}
