package com.example.sales.client;

import com.netflix.graphql.dgs.client.CustomGraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLClient;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.HttpResponse;
import org.intellij.lang.annotations.Language;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@Component
public class ProductGraphQLClient {

    private static final String PRODUCT_GRAPHQL_URL = "http://localhost:8081/graphql";

    private final RestTemplate restTemplate = new RestTemplate();

    private final CustomGraphQLClient graphQLClient = GraphQLClient.createCustom(PRODUCT_GRAPHQL_URL,  (url, headers, body) -> {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::addAll);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, httpHeaders), String.class);
        return new HttpResponse(exchange.getStatusCode().value(), exchange.getBody());
    });

    public GraphQLResponse fetchGraphQLResponse(@Language("graphql") String query,
                                                String operationName,
                                                Map<String, ? extends Object> variablesMap) {
        return graphQLClient.executeQuery(query, Optional.ofNullable(variablesMap).orElse(Collections.emptyMap()), operationName);
    }

}
