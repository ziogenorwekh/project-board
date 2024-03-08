package com.example.projectboard.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.projectboard.security.CustomizedUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${token.secret}")
    private String secret;

    private final UserDetailsService userService;


    public Map<String, String> generateToken(CustomizedUserDetails customizedUserDetails) {
        String jwt = JWT.create().withIssuer(customizedUserDetails.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .sign(Algorithm.HMAC256(secret.getBytes()));

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("access", jwt);
        userInfo.put("username", customizedUserDetails.getUsername());
        userInfo.put("userId", customizedUserDetails.getUserId());
        if (customizedUserDetails.getUser().hasRole("ROLE_ADMIN")) {
            userInfo.put("admin", "authenticated");
        }
        return userInfo;
    }

    /**
     * Check token validity using JwtTokenProvider
     * @param token
     * @return
     */
    public CustomizedUserDetails validateToken(String token) {
        String email = JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getIssuer();

        return (CustomizedUserDetails) userService.loadUserByUsername(email);
    }
}
