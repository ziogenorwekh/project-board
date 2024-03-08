package com.example.projectboard.service;

import com.example.projectboard.dto.PostDto;
import com.example.projectboard.repository.PostRepository;
import com.example.projectboard.repository.UserRepository;
import com.example.projectboard.vo.post.PostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestPostService {

    private final PostRepository postRepository;
    private final PostServiceImpl postService;

    @Autowired
    public TestPostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postService = new PostServiceImpl(postRepository, userRepository);
    }

    @Transactional
    public PostDto save(PostRequest postRequest, String userId) {
        return postService.save(postRequest, userId);
    }

    @Transactional
    public void delete(String postId, String userId) {
        postService.delete(postId, userId);
    }

    @Transactional
    public void deleteAll() {
        postRepository.deleteAll();
    }
}
