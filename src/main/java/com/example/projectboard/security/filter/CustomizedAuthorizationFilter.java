package com.example.projectboard.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.projectboard.security.CustomizedUserDetails;
import com.example.projectboard.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class CustomizedAuthorizationFilter extends BasicAuthenticationFilter {


    private final JwtTokenProvider jwts;

    public CustomizedAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwts) {
        super(authenticationManager);
        this.jwts = jwts;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(AUTHORIZATION);

//        /login Path is ignored header

        if (request.getServletPath().equals("/login") || header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = this.getAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    @SneakyThrows
    private Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String token = "";
        UsernamePasswordAuthenticationToken authenticationToken = null;
        try {
            token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
            CustomizedUserDetails userDetails = jwts.validateToken(token);

            authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails.getUser(),
                            null, userDetails.getAuthorities());
        } catch (JWTDecodeException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        catch (TokenExpiredException e) {
            response.setStatus(HttpStatus.GONE.value());
        }
        catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        return authenticationToken;
    }
}
