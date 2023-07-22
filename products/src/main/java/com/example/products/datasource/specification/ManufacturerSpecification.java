package com.example.products.datasource.specification;

import com.example.products.datasource.entity.Manufacturer;
import org.springframework.data.jpa.domain.Specification;

public class ManufacturerSpecification extends BaseSpecification {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_ORIGIN_COUNTRY = "originCountry";

    public static Specification<Manufacturer> nameContainsIgnoringCase(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get(FIELD_NAME)), contains(keyword.toLowerCase())
        );
    }

    public static Specification<Manufacturer> originCountryContainsIgnoringCase(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get(FIELD_ORIGIN_COUNTRY)), contains(keyword.toLowerCase())
        );
    }
}
