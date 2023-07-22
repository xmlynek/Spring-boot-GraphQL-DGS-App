package com.example.products.resolver;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.SeriesInput;
import com.example.products.datasource.entity.Series;
import com.example.products.service.SeriesQueryService;
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
public class SeriesResolver {

    private final SeriesQueryService seriesQueryService;

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.Series
    )
    public List<Series> getSeries(@InputArgument(name = "seriesInput") Optional<SeriesInput> seriesInput) {
        return seriesQueryService.findSeries(seriesInput);
    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.SeriesPagination
    )
    public Connection<Series> getSeriesPagination(@InputArgument Optional<SeriesInput> seriesInput,
                                                  DataFetchingEnvironment env,
                                                  @InputArgument Integer first,
                                                  @InputArgument Integer last,
                                                  @InputArgument String after,
                                                  @InputArgument String before
    ) {
        var fullResult =  seriesQueryService.findSeries(seriesInput);

        return new SimpleListConnection<>(fullResult).get(env);
    }
}
