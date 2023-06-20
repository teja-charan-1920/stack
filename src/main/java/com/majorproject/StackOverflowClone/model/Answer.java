package com.majorproject.StackOverflowClone.model;

import jakarta.persistence.*;
import jdk.jshell.spi.ExecutionControl;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "answers")
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;
    private String answer;
    private boolean isAccepted;
    private Long votes;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @JoinTable(
            name = "answers_votedup",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> votedUpByUsers;
    @ManyToMany
    @JoinTable(
            name = "answers_voteddown",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> votedDownByUsers;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id",referencedColumnName = "answer_id")
    private Set<Comment> comments;


}
