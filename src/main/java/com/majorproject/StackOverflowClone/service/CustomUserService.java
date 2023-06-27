package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.repository.UserRepository;
import com.majorproject.StackOverflowClone.security.oauth.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserService extends DefaultOAuth2UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new CustomUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);
        return user.map(CustomUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + email));
    }
}
