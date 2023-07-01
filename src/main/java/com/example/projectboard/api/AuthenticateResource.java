package com.example.projectboard.api;

import com.example.projectboard.security.CustomizedUserDetails;
import com.example.projectboard.security.jwt.JwtTokenProvider;
import com.example.projectboard.vo.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "http://43.200.8.149", methods = {RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.POST}, allowedHeaders = "*", allowCredentials = "true")
public class AuthenticateResource {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthenticateResource(AuthenticationManager authenticationManager,
                                JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
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

}
