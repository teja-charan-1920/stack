package com.majorproject.StackOverflowClone.specification;

import com.majorproject.StackOverflowClone.model.Tag;
import org.springframework.data.jpa.domain.Specification;

public class TagSpecification {
    public static Specification<Tag> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }

}
