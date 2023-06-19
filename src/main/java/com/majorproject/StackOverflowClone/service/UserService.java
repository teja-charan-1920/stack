package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
