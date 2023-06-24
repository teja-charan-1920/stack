package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.AuthenticationProvider;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired

    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void setNofVotesByUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        user.setVotes((long) (user.getVotedDownAnswers().size() + user.getVotedUpAnswers().size() + user.getVotedDownQuestions().size() + user.getVotedUpQuestions().size()));
        updateUser(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void addUserThroughOAuth(String email,String username){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        userRepository.save(user);
    }
}
