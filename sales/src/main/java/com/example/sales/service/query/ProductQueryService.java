package com.example.sales.service.query;

import com.course.graphql.generated.types.ModelSimple;
import com.example.sales.client.ProductGraphQLClient;
import com.example.sales.client.feign.ModelsClient;
import com.example.sales.contants.ProductsConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProductQueryService {

    private final ProductGraphQLClient productGraphQLClient;

    private final ModelsClient modelsClient;

    private final ObjectMapper objectMapper;

    public Map<String, ModelSimple> loadSimpleModels(Set<String> modelUuids) {
        var variablesMap = Map.ofEntries(
                Map.entry(ProductsConstants.VARIABLE_NAME_MODEL_UUIDS, modelUuids)
        );

        try {
            var simpleModelGraphQLResponse = productGraphQLClient.fetchGraphQLResponse(
                    ProductsConstants.QUERY_SIMPLE_MODELS, ProductsConstants.OPERATION_NAME_SIMPLE_MODELS, variablesMap);

            JsonNode jsonNode = objectMapper.readTree(simpleModelGraphQLResponse.getJson());
            String simpleModelsJson = jsonNode.get("data").get("simpleModels").toString();
            var listSimpleModels = objectMapper.readValue(simpleModelsJson, new TypeReference<List<ModelSimple>>() {});

            return Maps.uniqueIndex(listSimpleModels, ModelSimple::getUuid);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public Map<String, ModelSimple> loadSimpleModelsRest(Set<String> modelUuids) {
        try {
            List<ModelSimple> simpleModels = modelsClient.getSimpleModels(String.join(",", modelUuids));
            return Maps.uniqueIndex(simpleModels, ModelSimple::getUuid);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}
