package com.majorproject.StackOverflowClone.specification;

import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.Tag;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Set;

public class QuestionSpecification {
    public Specification<Question> filterPostsOnKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            String lowercaseKeyword = "%" + keyword.toLowerCase() + "%";
            Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), lowercaseKeyword);
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), lowercaseKeyword);

            Subquery<Long> tagSubquery = query.subquery(Long.class);
            Root<Question> tagSubqueryRoot = tagSubquery.from(Question.class);
            Join<Question, Tag> tagJoin = tagSubqueryRoot.join("tags");
            tagSubquery.select(tagSubqueryRoot.get("id"))
                    .where(criteriaBuilder.like(criteriaBuilder.lower(tagJoin.get("name")), lowercaseKeyword));
            Predicate tagPredicate = criteriaBuilder.in(root.get("id")).value(tagSubquery);

            return criteriaBuilder.or(descriptionPredicate, titlePredicate, tagPredicate);
        };
    }

    public Specification<Question> getQuestionsInLastDays(int days) {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime lastHrsQuestions = LocalDateTime.now().minusDays(days);
            return criteriaBuilder.greaterThan(root.get("createdAt"), lastHrsQuestions);
        };
    }

    public Specification<Question> getQuestionsWithTag(String tag) {
        return (root, query, criteriaBuilder) -> {
            Join<Question, Tag> tagJoin = root.join("tags", JoinType.INNER);
            Path<String> tagPath = tagJoin.get("name");
            return criteriaBuilder.equal(tagPath, tag);
        };
    }

    public Specification<Question> getQuestionsWithTags(Set<Tag> tags) {
        return (root, query, criteriaBuilder) -> {
            Join<Question, Tag> tagJoin = root.join("tags", JoinType.INNER);
            return tagJoin.in(tags);
        };
    }
}
