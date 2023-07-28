package com.example.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GraphqlSalesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlSalesApplication.class, args);
    }

}
