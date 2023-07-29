package com.example.sales.resolver;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.Model;
import com.example.sales.service.query.SalesOrderItemQueryService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsEntityFetcher;
import lombok.AllArgsConstructor;

import java.util.Map;

@DgsComponent
@AllArgsConstructor
public class ModelResolver {

    private final SalesOrderItemQueryService salesOrderItemQueryService;

    @DgsEntityFetcher(name = DgsConstants.MODEL.TYPE_NAME)
    public Model modelSalesPercentage(Map<String, Object> values) {
        return Model.newBuilder()
                .uuid(values.get(DgsConstants.MODEL.Uuid).toString())
                .build();
    }

    @DgsData(
            parentType = DgsConstants.MODEL.TYPE_NAME,
            field = DgsConstants.MODEL.SalesPercentage
    )
    public double modelSalesPercentage(DgsDataFetchingEnvironment env) {
        Model source = env.getSource();

        return salesOrderItemQueryService.findModelSalesPercentage(source.getUuid());
    }
}
