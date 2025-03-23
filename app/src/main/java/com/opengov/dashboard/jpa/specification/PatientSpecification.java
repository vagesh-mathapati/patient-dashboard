package com.opengov.dashboard.jpa.specification;

import com.opengov.dashboard.jpa.dao.PatientDao;
import com.opengov.dashboard.model.FilterCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class PatientSpecification implements Specification<PatientDao> {
    private final Map<String, FilterCriteria> filters;

    public PatientSpecification(Map<String, FilterCriteria> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<PatientDao> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction(); // Default to `AND` conditions

        for (Map.Entry<String, FilterCriteria> entry : filters.entrySet()) {
            String field = entry.getKey();
            FilterCriteria criteria = entry.getValue();

            switch (field) {
                case "age":
                    if (criteria.getOperator().equals(">")) {
                        predicate = cb.and(predicate, cb.greaterThan(root.get("age"), (Integer) criteria.getValue()));
                    } else if (criteria.getOperator().equals("<")) {
                        predicate = cb.and(predicate, cb.lessThan(root.get("age"), (Integer) criteria.getValue()));
                    }
                    break;

                case "diagnosis":
                    if (criteria.getOperator().equals("CONTAINS")) {
                        predicate = cb.and(predicate, cb.like(cb.lower(root.get("diagnosis")),
                                "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    } else if (criteria.getOperator().equals("EQUALS")) {
                        predicate = cb.and(predicate, cb.equal(root.get("diagnosis"), criteria.getValue()));
                    }
                    break;

                case "doctor":
                    if (criteria.getOperator().equals("EQUALS")) {
                        predicate = cb.and(predicate, cb.equal(root.get("doctor").get("lastName"), criteria.getValue()));
                    }
                    break;

                case "lastVisitAfter":
                    if (criteria.getOperator().equals("AFTER")) {
                        predicate = cb.and(predicate, cb.greaterThan(root.get("lastVisit"), (String) criteria.getValue()));
                    }
                    break;

                default:
                    break;
            }
        }
        return predicate;
    }

}
