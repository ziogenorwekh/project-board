package com.example.projectboard.api;

import com.example.projectboard.domain.User;
import com.example.projectboard.exception.CustomizedResponseException;
import com.example.projectboard.security.CustomizedUserDetails;
import com.example.projectboard.security.jwt.JwtTokenProvider;
import com.example.projectboard.vo.ExceptionResponse;
import com.example.projectboard.vo.LoginRequest;
import com.example.projectboard.vo.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.projectboard.api.UserResource.applyResponseFieldFilter;


@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "http://43.200.8.149", methods = {RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.POST}, allowedHeaders = "*", allowCredentials = "true")
public class AuthenticateResource {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public AuthenticateResource(AuthenticationManager authenticationManager,
                                JwtTokenProvider tokenProvider,
                                UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "로그인", description = "회원 로그인")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) throws BadCredentialsException {

        UsernamePasswordAuthenticationToken token;
        CustomizedUserDetails userDetails = null;
        token = new
                UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        userDetails = (CustomizedUserDetails) authenticate.getPrincipal();
        Map<String, String> userInfo = tokenProvider.generateToken(userDetails);
        return ResponseEntity.ok(userInfo);
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

        UserResponse userResponse = new ModelMapper().map(userDetailsService.loadUserByUsername(user.getEmail()),
                UserResponse.class);

        return ResponseEntity.ok()
                .body(applyResponseFieldFilter(userResponse, "userId", "username"));
    }

}
