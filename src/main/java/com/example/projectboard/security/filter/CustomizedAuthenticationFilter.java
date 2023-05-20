package com.example.projectboard.security.filter;

import com.example.projectboard.security.CustomizedUserDetails;
import com.example.projectboard.security.jwt.JwtTokenProvider;
import com.example.projectboard.vo.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomizedAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwt;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        LoginRequest loginRequest;
        UsernamePasswordAuthenticationToken token = null;
        try {
            loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            token = new
                    UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (IOException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            FilterExceptionHandler.exceptionThrow("invalidate data type");
        }

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        CustomizedUserDetails userDetails = (CustomizedUserDetails) authResult.getPrincipal();
        Map<String, String> userInfo = jwt.generateToken(userDetails);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), userInfo);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        FilterExceptionHandler.exceptionHandler(failed.getMessage(), response);
    }
}
