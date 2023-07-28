package com.example.projectboard.service;

import com.example.projectboard.domain.Comment;
import com.example.projectboard.domain.Post;
import com.example.projectboard.domain.User;
import com.example.projectboard.dto.PostDto;
import com.example.projectboard.exception.CustomizedResponseException;
import com.example.projectboard.repository.PostRepository;
import com.example.projectboard.repository.UserRepository;
import com.example.projectboard.vo.post.CommentRequest;
import com.example.projectboard.vo.post.PostRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    /**
     * @param postRequest current user's post data
     * @param userId current user
     * @return success post
     */
    @Override
    @Transactional
    public PostDto save(PostRequest postRequest, String userId) {

        User user = userRepository.findUserByUserId(userId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "user not in database."));

        Post post = Post.createPost(postRequest, user);
        postRepository.save(post);

        return mappingToDto(post);
    }

    /**
     * @param commentRequest user's comment data
     * @param postId         will be commented post
     */
    @Override
    @Transactional
    public void createComment(CommentRequest commentRequest, String postId) {

        Post post = postRepository.findPostByPostId(postId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "post not in database")
        );
        Comment.createComment(commentRequest, post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto findOne(String postId) {

        Post post = postRepository.findPostByPostId(postId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "post not in database."));

        return mappingToDto(post);
    }


    @Override
    @Transactional(readOnly = true)
    public List<PostDto> findAll() {
        return postRepository.findAll().stream().map(this::mappingToDto).collect(Collectors.toList());
    }

    /**
     * @param postId      current postId
     * @param postRequest modifying post data
     * @param userId      current userId
     * @return modified post data
     */
    @Override
    @Transactional
    public void updatePost(String postId, PostRequest postRequest, String userId) {
        Post post = postRepository.findPostByPostId(postId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "post not in database"));
        post.validateOwnPost(userId);
        post.update(postRequest);
    }

    /**
     * own post can delete and admin also do
     * @param postId Current postId
     * @param userId Login userId
     */
    @Override
    @Transactional
    public void delete(String postId, String userId) {
        Post post = postRepository.findPostByPostId(postId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "post not in database"));

        User user = userRepository.findUserByUserId(userId).orElseThrow(() ->
                new CustomizedResponseException(HttpStatus.NOT_FOUND, "user not in database."));

        if (!user.hasRole("ROLE_ADMIN")) {
            post.validateOwnPost(userId);
        }
        post.delete();
        postRepository.delete(post);
    }

    private PostDto mappingToDto(Post post) {
        ModelMapper mapper = new ModelMapper();
        TypeMap<Post, PostDto> map = mapper.typeMap(Post.class, PostDto.class)
                .addMappings(mapping -> {
                    mapping.map(source -> source.getUser().getUsername(), PostDto::setUsername);
                    mapping.map(source -> source.getUser().getUserId(), PostDto::setUserId);
                });
        return map.map(post);
    }
}
