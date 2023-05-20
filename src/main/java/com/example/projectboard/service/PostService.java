package com.example.projectboard.service;


import com.example.projectboard.dto.PostDto;
import com.example.projectboard.vo.post.CommentRequest;
import com.example.projectboard.vo.post.PostRequest;

import java.util.List;

public interface PostService {

    PostDto save(PostRequest postRequest, String userId);

    void createComment(CommentRequest commentRequest, String postId);

    PostDto findOne(String postId);

    List<PostDto> findAll();

    void updatePost(String postId,PostRequest postRequest, String userId);


    void delete(String postId,String userId);
}
