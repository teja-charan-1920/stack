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

    public void setNofVotesByUser(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        user.setVotes((long) (user.getVotedDownAnswers().size()+user.getVotedUpAnswers().size()+user.getVotedDownQuestions().size()+user.getVotedUpQuestions().size()));
        updateUser(user);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public User saveUser(User user) {
      return userRepository.save(user);
    }
}
