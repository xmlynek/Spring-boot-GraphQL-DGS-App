package com.example.sales.dataloader;

import com.course.graphql.generated.types.ModelSimple;
import com.example.sales.client.feign.ModelsClient;
import com.example.sales.contants.DataLoaderConstants;
import com.example.sales.service.query.ProductQueryService;
import com.netflix.graphql.dgs.DgsDataLoader;
import graphql.com.google.common.collect.Maps;
import org.dataloader.MappedBatchLoader;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@DgsDataLoader(name = "simpleModelRestDataLoader")
public class SimpleModelRestDataLoader implements MappedBatchLoader<String, ModelSimple> {

    private final ModelsClient modelsClient;

    private final Executor dataLoaderThreadPoolExecutor;

    public SimpleModelRestDataLoader(ModelsClient modelsClient,
                                 @Qualifier(DataLoaderConstants.THREAD_POOL_EXECUTOR_NAME) Executor dataLoaderThreadPoolExecutor) {
        this.modelsClient = modelsClient;
        this.dataLoaderThreadPoolExecutor = dataLoaderThreadPoolExecutor;
    }

    @Override
    public CompletionStage<Map<String, ModelSimple>> load(Set<String> keys) {
        return CompletableFuture.supplyAsync(() -> {
            List<ModelSimple> simpleModels = modelsClient.getSimpleModels(String.join(",", keys));
            return Maps.uniqueIndex(simpleModels, ModelSimple::getUuid);
        }, dataLoaderThreadPoolExecutor);
    }
}
