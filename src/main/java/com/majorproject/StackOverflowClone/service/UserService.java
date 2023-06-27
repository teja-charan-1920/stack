package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.dto.PageDto;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.repository.UserRepository;
import com.majorproject.StackOverflowClone.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        if(getByEmail(user.getEmail()) != null) {
            throw new RuntimeException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void addUserThroughOAuth(String email, String name) {
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public PageDto getAllUsers(String search, String sortBy, int page) {
        Pageable pageable = PageRequest.of(page - 1, 36, Sort.by(Sort.Direction.DESC, sortBy));
        if (sortBy.equals("username"))
            pageable = PageRequest.of(page - 1, 36, Sort.by(Sort.Direction.ASC, sortBy));
        Specification<User> specification = new UserSpecification().getAllUserSpecification(search);
        Page<User> userPage = null;
        if (search == null) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.findAll(specification, pageable);
        }

        PageDto pageDto = new PageDto();
        pageDto.setPage(page);
        pageDto.setSearch(search);
        pageDto.setUsers(userPage.getContent());
        pageDto.setSortBy(sortBy);
        pageDto.setTotalPages(userPage.getTotalPages());
        return pageDto;
    }
}
