package com.example.products.service;

import com.course.graphql.generated.types.ManufacturerInput;
import com.example.products.datasource.entity.Manufacturer;
import com.example.products.datasource.repository.ManufacturerRepository;
import com.example.products.datasource.specification.ManufacturerSpecification;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ManufacturerQueryService {

    private final ManufacturerRepository manufacturerRepository;

    public List<Manufacturer> findManufacturers(Optional<ManufacturerInput> input) {
        var manufacturerInput = input.orElse(new ManufacturerInput());

        var specification = Specification.where(
                StringUtils.isNotBlank(manufacturerInput.getName()) ?
                        ManufacturerSpecification.nameContainsIgnoringCase(manufacturerInput.getName()) : null
        ).and(
                StringUtils.isNotBlank(manufacturerInput.getOriginCountry()) ?
                        ManufacturerSpecification.originCountryContainsIgnoringCase(manufacturerInput.getOriginCountry()) :
                        null
        );

        List<Sort.Order> orders = ManufacturerSpecification.sortOrdersFrom(manufacturerInput.getSortBy());

        return manufacturerRepository.findAll(specification, Sort.by(orders));
    }
}
