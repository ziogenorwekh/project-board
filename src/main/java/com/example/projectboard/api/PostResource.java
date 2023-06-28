package com.example.projectboard.api;

import com.example.projectboard.domain.User;
import com.example.projectboard.dto.PostDto;
import com.example.projectboard.service.PostService;
import com.example.projectboard.vo.ExceptionResponse;
import com.example.projectboard.vo.post.CommentRequest;
import com.example.projectboard.vo.post.PostListResponse;
import com.example.projectboard.vo.post.PostRequest;
import com.example.projectboard.vo.post.PostResponse;
import com.example.projectboard.vo.user.UserResponse;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "게시글 API")
@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://43.200.8.149:3000", methods = {RequestMethod.GET, RequestMethod.PUT,RequestMethod.DELETE,RequestMethod.POST}, allowedHeaders = "*")
public class PostResource {

    private final PostService postService;

    @Autowired
    public PostResource(PostService postService) {
        this.postService = postService;
    }


    @RequestMapping(path = "/posts", method = RequestMethod.POST)
    @Operation(summary = "글 작성", description = "글 작성 폼에 맞는 글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "글 작성 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "폼 형식에 맞지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<Void> createPost(@Validated @RequestBody
                                           @Parameter(description = "작성할 데이터") PostRequest postRequest,
                                           @AuthenticationPrincipal
                                           @Parameter(description = "로그인 정보") User user) {

        PostDto save = postService.save(postRequest, user.getUserId());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(save.getPostId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "글 작성 성공",
                    content = @Content(schema = @Schema()))
    })
    @RequestMapping(path = "/comments/{postId}", method = RequestMethod.POST)
    public ResponseEntity<Void> createComment(@Validated @RequestBody @Parameter(description = "작성할 댓글 데이터")
                                              CommentRequest commentRequest,
                                              @PathVariable @Parameter(description = "댓글 작성할 게시글 ID") String postId) {
        postService.createComment(commentRequest, postId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(path = "/posts/{postId}", method = RequestMethod.GET)
    @Operation(summary = "글 단건 조회", description = "UUID 글 단건 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "404", description = "찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<MappingJacksonValue> retrievePost(@PathVariable
                                                            @Parameter(description = "조회할 게시글 ID") String postId) {

        PostDto postDto = postService.findOne(postId);
        PostResponse postResponse = new PostResponse(postDto);

        EntityModel<PostResponse> model = EntityModel.of(postResponse);

        WebMvcLinkBuilder updateLinkBuilder = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).updatePost(postId, null, null));
        model.add(updateLinkBuilder.withRel("update"));

        WebMvcLinkBuilder deleteLinkBuilder = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).deletePost(postId, null));
        model.add(deleteLinkBuilder.withRel("delete"));

        return ResponseEntity.ok().body(applyResponseFieldFilter(model));
    }


    @RequestMapping(path = "/posts", method = RequestMethod.GET)
    @Operation(summary = "글 전체 조회", description = "글 전체 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = PostListResponse.class)))
    public ResponseEntity<MappingJacksonValue> retrieveAllPosts() {
        List<PostDto> list = postService.findAll();

        List<PostResponse> postResponses = list.stream()
                .map(PostResponse::new).toList();

        MappingJacksonValue jacksonValue =
                applyResponseFieldFilter(postResponses, "title", "username", "postId", "postedBy");

        return ResponseEntity.status(HttpStatus.OK).body(jacksonValue);
    }


    @RequestMapping(path = "/posts/{postId}", method = RequestMethod.PUT)
    @Operation(summary = "글 수정", description = "폼 형식에 맞는 글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "폼에 맞지 않는 형식",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<Void> updatePost(@PathVariable @Parameter(description = "수정할 포스트 ID") String postId,
                                           @Validated @RequestBody @Parameter(description = "수정할 정보")
                                           PostRequest postRequest,
                                           @AuthenticationPrincipal @Parameter(description = "로그인 정보") User user) {

        postService.updatePost(postId, postRequest, user.getUserId());
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(path = "/posts/{postId}", method = RequestMethod.DELETE)
    @Operation(summary = "글 삭제", description = "글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "삭제 성공", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "접근 권한 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<Void> deletePost(@PathVariable @Parameter(description = "삭제할 포스트 ID") String postId
            , @AuthenticationPrincipal @Parameter(description = "로그인 정보") User user) {

        postService.delete(postId, user.getUserId());

        return ResponseEntity.noContent().build();
    }


    private MappingJacksonValue applyResponseFieldFilter(Object response, String... includedFields) {
        SimpleBeanPropertyFilter filter = includedFields.length > 0 ?
                SimpleBeanPropertyFilter.filterOutAllExcept(includedFields) :
                SimpleBeanPropertyFilter.serializeAll();
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("post", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(response);
        jacksonValue.setFilters(filterProvider);

        return jacksonValue;
    }

}
