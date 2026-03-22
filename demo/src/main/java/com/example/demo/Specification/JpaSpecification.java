package com.example.demo.Specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.model.Product;

import jakarta.persistence.criteria.Predicate;

public class JpaSpecification {

    public static Specification<Product> build(
            String search,
            Double price,
            Double mrp) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), like));
            }

            if (price != null) {
                predicates.add(cb.equal(root.get("price"), price));
            }

            if (mrp != null) {
                predicates.add(cb.equal(root.get("mrp"), mrp));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}