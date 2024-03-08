package com.example.projectboard.repository;

import com.example.projectboard.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("select p from Post p where p.postId = ?1")
    Optional<Post> findPostByPostId(String postId);
}
