package com.example.sales.datasource.specification;

import com.example.sales.datasource.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CustomerSpecification extends BaseSpecification {

    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_UUID = "uuid";

    public static Specification<Customer> emailEqualsIgnoreCase(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get(FIELD_EMAIL)), email.toLowerCase());
    }

    public static Specification<Customer> uuidEqualsIgnoreCase(String uuid) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(FIELD_UUID), UUID.fromString(uuid.toLowerCase()));
    }
}
