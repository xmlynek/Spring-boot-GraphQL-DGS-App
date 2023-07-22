package com.example.products.datasource.specification;

import com.example.products.datasource.entity.Series;
import org.springframework.data.jpa.domain.Specification;

public class SeriesSpecification extends BaseSpecification {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_MANUFACTURER = "manufacturer";

    public static Specification<Series> nameContainsIgnoringCase(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get(FIELD_NAME)), contains(keyword.toLowerCase())
        );
    }

    public static Specification<Series> manufacturerNameContainsIgnoringCase(String keyword) {
        return (root, query, criteriaBuilder) -> {
            var joinSeriesManufacturer = root.join(FIELD_MANUFACTURER);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(joinSeriesManufacturer.get(ManufacturerSpecification.FIELD_NAME)),
                    contains(keyword.toLowerCase())
            );
        };
    }

    public static Specification<Series> manufacturerOriginCountryContainsIgnoringCase(String keyword) {
        return (root, query, criteriaBuilder) -> {
            var joinSeriesManufacturer = root.join(FIELD_MANUFACTURER);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(joinSeriesManufacturer.get(ManufacturerSpecification.FIELD_ORIGIN_COUNTRY)),
                    contains(keyword.toLowerCase())
            );
        };
    }

}
