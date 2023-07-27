package com.example.products.service;

import com.course.graphql.generated.types.*;
import com.example.products.datasource.entity.Model;
import com.example.products.datasource.repository.ModelRepository;
import com.example.products.datasource.specification.ModelSpecification;
import com.example.products.mapper.ModelMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ModelQueryService {

    private final ModelRepository modelRepository;

    private final ModelMapper modelMapper;

    public List<Model> findModels(Optional<ModelInput> input, Optional<NumericComparisonInput> numericComparisonInput) {
        var modelInput = input.orElse(new ModelInput());

        var modelSpecification = modelSpecificationFromInput(modelInput);
        var priceSpecification = priceSpecificationFromInput(numericComparisonInput);

        List<Sort.Order> orders = ModelSpecification.sortOrdersFrom(modelInput.getSortBy());

        return modelRepository.findAll(modelSpecification.and(priceSpecification), Sort.by(orders));
    }

    public Page<Model> findModels(Optional<ModelInput> input,
                                  Optional<NumericComparisonInput> numericComparisonInput,
                                  Integer page,
                                  Integer size) {
        var modelInput = input.orElse(new ModelInput());

        var modelSpecification = modelSpecificationFromInput(modelInput);
        var priceSpecification = priceSpecificationFromInput(numericComparisonInput);

        List<Sort.Order> sortOrders = ModelSpecification.sortOrdersFrom(modelInput.getSortBy());

        var pagination = PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(50),
                Sort.by(sortOrders)
        );

        return modelRepository.findAll(modelSpecification.and(priceSpecification), pagination);
    }

    public List<ModelSimple> findSimpleModels(List<String> modelUuids) {
        var models = modelRepository.findAllById(modelUuids.stream().map(UUID::fromString).toList());

        return models.stream().map(modelMapper::modelEntityToSimpleModel).toList();
    }

    private Specification<Model> modelSpecificationFromInput(ModelInput modelInput) {
        var seriesInput = modelInput.getSeries() == null ? new SeriesInput() : modelInput.getSeries();
        var manufacturerInput = seriesInput.getManufacturer() == null ? new ManufacturerInput() : seriesInput.getManufacturer();

        return Specification.where(StringUtils.isNotBlank(modelInput.getName()) ?
                ModelSpecification.nameContainsIgnoringCase(modelInput.getName()) : null
        ).and(
                StringUtils.isNotBlank(manufacturerInput.getName()) ?
                        ModelSpecification.manufacturerNameContainsIgnoringCase(manufacturerInput.getName()) :
                        null
        ).and(
                StringUtils.isNotBlank(manufacturerInput.getOriginCountry()) ?
                        ModelSpecification.manufacturerOriginCountryContainsIgnoringCase(
                                manufacturerInput.getOriginCountry()) :
                        null
        ).and(
                StringUtils.isNotBlank(seriesInput.getName()) ?
                        ModelSpecification.seriesNameContainsIgnoringCase(seriesInput.getName()) :
                        null
        ).and(
                modelInput.getIsAvailable() != null ?
                        ModelSpecification.available(modelInput.getIsAvailable()) :
                        null
        ).and(
                modelInput.getTransmission() != null ?
                        ModelSpecification.transmissionEquals(modelInput.getTransmission()) :
                        null
        ).and(
                CollectionUtils.isEmpty(modelInput.getExteriorColors()) ?
                        null :
                        ModelSpecification.exteriorColorsLikeIgnoreCase(modelInput.getExteriorColors())
        );
    }

    private Specification<Model> priceSpecificationFromInput(Optional<NumericComparisonInput> numericComparisonInput) {
        return numericComparisonInput.map(
                numericInput -> switch (numericInput.getOperator()) {
                    case LESS_THAN_EQUALS -> ModelSpecification.priceLessThanEquals(numericInput.getValue());
                    case GREATER_THAN_EQUALS -> ModelSpecification.priceGreaterThanEquals(numericInput.getValue());
                    case BETWEEN_INCLUSIVE -> ModelSpecification.priceBetween(numericInput.getValue(),
                            numericInput.getHighValue() == null || numericInput.getHighValue() < numericInput.getValue() ?
                                    numericInput.getValue() + 1 :
                                    numericInput.getHighValue()
                    );
                }).orElse(null);
    }
}
