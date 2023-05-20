package com.example.projectboard.security;

import com.example.projectboard.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
public class CustomizedUserDetails implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add((GrantedAuthority) role::getRole));
        return authorities;
    }

    public String getUserId() {
        return user.getUserId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
