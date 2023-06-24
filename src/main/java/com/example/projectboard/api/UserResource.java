package com.example.projectboard.api;

import com.example.projectboard.domain.User;
import com.example.projectboard.dto.UserDto;
import com.example.projectboard.exception.CustomizedResponseException;
import com.example.projectboard.service.UserService;
import com.example.projectboard.vo.*;
import com.example.projectboard.vo.user.UserListResponse;
import com.example.projectboard.vo.user.UserRequest;
import com.example.projectboard.vo.user.UserResponse;
import com.example.projectboard.vo.user.UserUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "사용자 API")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.17.0.1:*", methods = {RequestMethod.GET, RequestMethod.PUT,RequestMethod.DELETE,RequestMethod.POST}, allowedHeaders = "application/json")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @Operation(summary = "회원가입", description = "지정된 폼 형식에 맞는 회원 가입")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "지정된 폼 형식에 맞지 않음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "이메일 중복",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<?> createUser(@Validated @RequestBody @Parameter(description = "회원가입 회원 정보")
                                        UserRequest userRequest) {

        UserDto userDto = userService.save(userRequest);
        UserResponse userResponse = new ModelMapper().map(userDto, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applyResponseFieldFilter(userResponse, "userId", "email", "username"));
    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @Operation(summary = "회원 전체 조회", description = "등록된 회원 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"
                    , content = @Content(schema = @Schema(implementation = UserListResponse.class))),
    })
    public ResponseEntity<?> retrieveAllUsers() {
        List<UserDto> list = userService.findAll();
        List<UserResponse> responses = list.stream()
                .map(userDto -> new ModelMapper().map(userDto, UserResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(applyResponseFieldFilter(new UserListResponse(responses), "userId", "email", "username"));
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    @Operation(summary = "회원 조회", description = "회원 아이디를 통해 회원 검색")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 존재"
                    , content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 존재하지 않음"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<?> retrieveUser(@PathVariable @Parameter(description = "조회할 ID") String userId) {
        UserDto userDto = userService.findOne(userId);
        UserResponse userResponse = new ModelMapper().map(userDto, UserResponse.class);

        EntityModel<UserResponse> model = EntityModel.of(userResponse);

        WebMvcLinkBuilder updateLinkBuilder = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).updateUser(userId, null, null));
        model.add(updateLinkBuilder.withRel("update"));

        WebMvcLinkBuilder deleteLinkBuilder = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).deleteUser(userId, null));
        model.add(deleteLinkBuilder.withRel("delete"));

        return ResponseEntity.status(HttpStatus.OK).body(applyResponseFieldFilter(model));
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
    @Operation(summary = "회원 수정", description = "회원 아이디를 통해 회원 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "조건에 맞지 않는 폼 형식"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없는 접근"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않음"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "410", description = "토큰 만료"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<?> updateUser(@PathVariable @Parameter(description = "수정할 ID") String userId,
                                        @Validated @RequestBody @Parameter(description = "수정할 데이터")
                                        UserUpdateRequest userUpdateRequest,
                                        @AuthenticationPrincipal
                                        @Parameter(description = "로그인 정보") User user) {

        UserDto updated = userService.updateUserInfo(userId, userUpdateRequest, user.getUserId());
        UserResponse userResponse = new ModelMapper().map(updated, UserResponse.class);

        return ResponseEntity.accepted().body(applyResponseFieldFilter(userResponse));
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    @Operation(summary = "회원 삭제", description = "회원 아이디를 통해 회원 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없는 접근"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않음"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "410", description = "토큰 만료"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<Void> deleteUser(@PathVariable @Parameter(description = "삭제할 ID") String userId,
                                           @AuthenticationPrincipal @Parameter(description = "로그인 정보") User user) {

        userService.delete(userId, user.getUserId());
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/currents")
    @Operation(summary = "현재 유저 조회", description = "토큰을 이용한 현재 유저 상태 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"
                    , content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "410", description = "토큰 만료"
                    , content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<MappingJacksonValue> currentUser(@AuthenticationPrincipal
                                                           @Parameter(description = "로그인 정보")
                                                           User user) {
        if (user == null) {
            throw new CustomizedResponseException(HttpStatus.GONE, "token is expired.");
        }

        UserResponse userResponse = new ModelMapper().map(userService.findOne(user.getUserId()),
                UserResponse.class);

        return ResponseEntity.ok()
                .body(applyResponseFieldFilter(userResponse, "userId", "username"));
    }

    private MappingJacksonValue applyResponseFieldFilter(Object response, String... includedFields) {
        SimpleBeanPropertyFilter filter = includedFields.length > 0 ?
                SimpleBeanPropertyFilter.filterOutAllExcept(includedFields) :
                SimpleBeanPropertyFilter.serializeAll();
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("user", filter);

        MappingJacksonValue jacksonValue = new MappingJacksonValue(response);
        jacksonValue.setFilters(filterProvider);

        return jacksonValue;
    }
}
