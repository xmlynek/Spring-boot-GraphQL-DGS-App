package com.example.products.resolver;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.ModelInput;
import com.course.graphql.generated.types.ModelPagination;
import com.course.graphql.generated.types.ModelSimple;
import com.course.graphql.generated.types.NumericComparisonInput;
import com.example.products.datasource.entity.Model;
import com.example.products.service.ModelQueryService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@DgsComponent
@AllArgsConstructor
public class ModelResolver {

    private final ModelQueryService modelQueryService;


    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.Models
    )
    public List<Model> getModels(@InputArgument(name = "modelInput") Optional<ModelInput> modelInput,
                                 @InputArgument(name = "priceInput") Optional<NumericComparisonInput> numericComparisonInput) {

        return modelQueryService.findModels(modelInput, numericComparisonInput);
    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.ModelsPagination
    )
    public ModelPagination getModelsPagination(
            @InputArgument(name = "modelInput") Optional<ModelInput> modelInput,
            DataFetchingEnvironment env,
            @InputArgument(name = "priceInput") Optional<NumericComparisonInput> numericComparisonInput,
            @InputArgument Integer page,
            @InputArgument Integer size
    ) {
        Page<Model> modelsPage = modelQueryService.findModels(modelInput, numericComparisonInput, page, size);

        var modelConnection = new SimpleListConnection<>(modelsPage.getContent()).get(env);

        return ModelPagination.newBuilder()
                .modelConnection(modelConnection)
                .page(modelsPage.getNumber())
                .size(modelsPage.getSize())
                .totalElements(modelsPage.getTotalElements())
                .totalPages(modelsPage.getTotalPages())
                .build();
    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.SimpleModels
    )
    public List<ModelSimple> simpleModels(@InputArgument List<String> modelUuids) {
        return modelQueryService.findSimpleModels(modelUuids);
    }
}
