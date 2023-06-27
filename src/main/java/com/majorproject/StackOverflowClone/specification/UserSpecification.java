package com.majorproject.StackOverflowClone.specification;

import com.majorproject.StackOverflowClone.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public Specification<User> getAllUserSpecification(String keyword) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),"%"+keyword.toLowerCase()+"%");
    }
}
