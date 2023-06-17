package com.majorproject.StackOverflowClone.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "history")
@Getter
@Setter
public class History {
    @Id
    private LocalDateTime createdAt;
    private String action;
    private String comment;
}
