package com.majorproject.StackOverflowClone.specification;

import com.majorproject.StackOverflowClone.model.History;
import org.springframework.data.jpa.domain.Specification;

public class HistorySpecification {
    public Specification<History> getHistorySpecification(Long answerId) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return criteriaBuilder.equal(root.get("answer").get("answerId"),answerId );
        };
    }
}
