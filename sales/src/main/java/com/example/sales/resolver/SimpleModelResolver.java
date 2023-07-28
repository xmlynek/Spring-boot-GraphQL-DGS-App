package com.example.sales.resolver;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.ModelSimple;
import com.course.graphql.generated.types.SalesOrderItem;
import com.example.sales.contants.DataLoaderConstants;
import com.example.sales.dataloader.SimpleModelRestDataLoader;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

@DgsComponent
public class SimpleModelResolver {

    @DgsData(
            parentType = DgsConstants.SALESORDERITEM.TYPE_NAME,
            field = DgsConstants.SALESORDERITEM.ModelDetail
    )
    public CompletableFuture<ModelSimple> loadSimpleModels(DataFetchingEnvironment env) {
//        DataLoader<String, ModelSimple> simpleModelDataLoader = env.getDataLoader(DataLoaderConstants.SIMPLE_MODEL_DATA_LOADER_NAME);
        DataLoader<String, ModelSimple> simpleModelDataLoader = env.getDataLoader(DataLoaderConstants.SIMPLE_MODEL_REST_DATA_LOADER_NAME);
        SalesOrderItem salesOrderItem = env.getSource();

        return simpleModelDataLoader.load(salesOrderItem.getModelUuid());
    }
}
