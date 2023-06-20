package com.majorproject.StackOverflowClone.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(configurer ->
                        configurer
//                                .requestMatchers("/", "/api/blog/**","/post/{postId}", "/addComment/**", "/register" ,"/api/blog/post/{postId}/comments").permitAll()
//                                .requestMatchers("/newPost").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/register","/createAccount","/api/blog/post/{postId}/comment", "/addPost","/post/{postId}/comment").permitAll()
                                .anyRequest().permitAll())
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }


}
