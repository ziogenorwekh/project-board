package com.example.projectboard.security;

import com.example.projectboard.security.filter.CorsFilter;
import com.example.projectboard.security.filter.CustomizedAuthorizationFilter;
import com.example.projectboard.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwts;

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.httpBasic().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable();

        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/api/login").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/api/users")
                .permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET).permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST).hasAnyRole("USER", "ADMIN");
        http.authorizeHttpRequests().requestMatchers(HttpMethod.PUT).hasAnyRole("USER", "ADMIN");
        http.authorizeHttpRequests().requestMatchers(HttpMethod.DELETE).hasAnyRole("USER", "ADMIN");

        http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                .addFilter(new CustomizedAuthorizationFilter(authenticationManager(authenticationConfiguration()), jwts));

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomizedAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomizedAccessDeniedHandler());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            @Qualifier("customAuthenticationConfiguration") AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Qualifier("customAuthenticationConfiguration")
    public AuthenticationConfiguration authenticationConfiguration() {
        return new AuthenticationConfiguration();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
