package com.example.products.service;

import com.course.graphql.generated.types.ManufacturerInput;
import com.course.graphql.generated.types.SeriesInput;
import com.example.products.datasource.entity.Series;
import com.example.products.datasource.repository.SeriesRepository;
import com.example.products.datasource.specification.ManufacturerSpecification;
import com.example.products.datasource.specification.SeriesSpecification;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SeriesQueryService {

    private final SeriesRepository seriesRepository;

    public List<Series> findSeries(Optional<SeriesInput> input) {
        var seriesInput = input.orElse(new SeriesInput());
        var manufacturerInput = seriesInput.getManufacturer() == null ?
                new ManufacturerInput() : seriesInput.getManufacturer();

        var specification = Specification.where(
                StringUtils.isNotBlank(seriesInput.getName()) ?
                        SeriesSpecification.nameContainsIgnoringCase(seriesInput.getName()) : null
        ).and(
                StringUtils.isNotBlank(manufacturerInput.getName()) ?
                        SeriesSpecification.manufacturerNameContainsIgnoringCase(manufacturerInput.getName()) : null
        ).and(
                StringUtils.isNotBlank(manufacturerInput.getOriginCountry()) ?
                        SeriesSpecification.manufacturerOriginCountryContainsIgnoringCase(
                                manufacturerInput.getOriginCountry()
                        ) : null
        );

        List<Sort.Order> orders = SeriesSpecification.sortOrdersFrom(seriesInput.getSortBy());

        return seriesRepository.findAll(specification, Sort.by(orders));
    }

}
