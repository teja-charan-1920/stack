package com.majorproject.StackOverflowClone.security.oauth;

import com.majorproject.StackOverflowClone.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomUser implements OAuth2User, UserDetails {
    private final String username;
    private final String email;
    private String password;
    private Map<String, Object> attributes;

    public CustomUser(OAuth2User oAuth2User) {
        this.username = oAuth2User.getAttribute("name");
        this.email = oAuth2User.getAttribute("email");
        this.attributes = oAuth2User.getAttributes();
    }

    public CustomUser(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
