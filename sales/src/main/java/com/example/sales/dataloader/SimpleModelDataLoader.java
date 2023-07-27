package com.example.sales.dataloader;

import com.course.graphql.generated.types.ModelSimple;
import com.example.sales.contants.DataLoaderConstants;
import com.example.sales.service.query.ProductQueryService;
import com.netflix.graphql.dgs.DgsDataLoader;
import org.dataloader.MappedBatchLoader;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@DgsDataLoader(name = DataLoaderConstants.SIMPLE_MODEL_DATA_LOADER_NAME)
public class SimpleModelDataLoader implements MappedBatchLoader<String, ModelSimple> {

    private final ProductQueryService productQueryService;

    private final Executor dataLoaderThreadPoolExecutor;

    public SimpleModelDataLoader(ProductQueryService productQueryService,
                                 @Qualifier(DataLoaderConstants.THREAD_POOL_EXECUTOR_NAME) Executor dataLoaderThreadPoolExecutor) {
        this.productQueryService = productQueryService;
        this.dataLoaderThreadPoolExecutor = dataLoaderThreadPoolExecutor;
    }

    @Override
    public CompletionStage<Map<String, ModelSimple>> load(Set<String> keys) {
        return CompletableFuture.supplyAsync(
                () -> productQueryService.loadSimpleModels(keys), dataLoaderThreadPoolExecutor);
    }
}
