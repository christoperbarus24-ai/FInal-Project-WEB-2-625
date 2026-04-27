package com.example.productcrud.repository;

import com.example.productcrud.model.Product;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> withFilter(Long userId, String keyword, Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("userId"), userId));

            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + keyword.trim().toLowerCase() + "%"
                ));
            }

            if (categoryId != null) {
                var categoryJoin = root.join("category", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}