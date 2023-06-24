package com.majorproject.StackOverflowClone.security;

import com.majorproject.StackOverflowClone.service.CustomOAuth2UserService;
import com.majorproject.StackOverflowClone.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;
    
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/oauth2/**").permitAll()
                                .requestMatchers("/questions/ask").authenticated()
                                .requestMatchers("/css/**", "/images/**","/tinymce/**","/home","/questions/{id}","/","/questionView/{id}","/tags","/questions/tagged/{tag}","/login","/signup","/users")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                        .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                                .defaultSuccessUrl("/home"))
                        .oauth2Login(c -> c
                                .loginPage("/login")
                                .userInfoEndpoint(customizer ->
                                        customizer.userService(oAuth2UserService))
                                .successHandler(oAuth2LoginSuccessHandler))
                        .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .permitAll());
        return httpSecurity.build();
    }


}