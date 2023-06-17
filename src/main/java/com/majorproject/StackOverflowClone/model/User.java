package com.majorproject.StackOverflowClone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private Long reputation;
    private Long votes;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "user_id",name = "user_id")
    private Set<Question> questions;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "user_id",name = "user_id")
    private Set<Answer> answers;
    @ManyToMany(mappedBy = "votedUpByUsers")
    private Set<Answer> votedUpAnswers;
    @ManyToMany(mappedBy = "votedDownByUsers")
    private Set<Answer> votedDownAnswers;
    @ManyToMany(mappedBy = "votedUpByUsers")
    private Set<Question> votedUpQuestions;
    @ManyToMany(mappedBy = "votedDownByUsers")
    private Set<Question> votedDownQuestions;
}
