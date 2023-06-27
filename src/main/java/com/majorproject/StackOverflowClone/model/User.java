package com.majorproject.StackOverflowClone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private Long reputation = 0l;
    private Long votes = 0l;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Question> questions;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Answer> answers;
    @JsonIgnore
    @ManyToMany(mappedBy = "votedUpByUsers")
    private Set<Answer> votedUpAnswers;
    @JsonIgnore
    @ManyToMany(mappedBy = "votedDownByUsers")
    private Set<Answer> votedDownAnswers;
    @JsonIgnore
    @ManyToMany(mappedBy = "votedUpByUsers")
    private Set<Question> votedUpQuestions;
    @JsonIgnore
    @ManyToMany(mappedBy = "votedDownByUsers")
    private Set<Question> votedDownQuestions;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_badges",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id"))
    private Set<Badge> badges;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Comment> comments;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<History> history;
}
