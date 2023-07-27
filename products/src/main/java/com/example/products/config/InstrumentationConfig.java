package com.example.products.config;

import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstrumentationConfig {

    @Bean
    public Instrumentation maxQueryComplexityInstrumentation() {
        return new MaxQueryComplexityInstrumentation(100);
    }

    @Bean
    public Instrumentation maxQueryDepthInstrumentation() {
        return new MaxQueryDepthInstrumentation(15);
    }

    @Bean
    public Instrumentation tracingInstrumentation() {
        return new TracingInstrumentation();
    }
}
