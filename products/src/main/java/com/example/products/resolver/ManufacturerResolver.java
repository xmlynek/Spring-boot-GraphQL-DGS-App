package com.example.products.resolver;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.ManufacturerInput;
import com.example.products.datasource.entity.Manufacturer;
import com.example.products.service.ManufacturerQueryService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.relay.Connection;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@DgsComponent
@AllArgsConstructor
public class ManufacturerResolver {

    private final ManufacturerQueryService manufacturerQueryService;

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.Manufacturers
    )
    public List<Manufacturer> getManufacturers(@InputArgument(name = "manufacturerInput") Optional<ManufacturerInput> input) {
        return manufacturerQueryService.findManufacturers(input);
    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.ManufacturersPagination
    )
    public Connection<Manufacturer> getManufacturersPagination(
            @InputArgument Optional<ManufacturerInput> manufacturerInput,
            DataFetchingEnvironment dataFetchingEnvironment,
            @InputArgument Integer first,
            @InputArgument Integer last,
            @InputArgument String after,
            @InputArgument String before
    ) {
        var fullResult = manufacturerQueryService.findManufacturers(manufacturerInput);

        return new SimpleListConnection<>(fullResult).get(dataFetchingEnvironment);
    }
}
