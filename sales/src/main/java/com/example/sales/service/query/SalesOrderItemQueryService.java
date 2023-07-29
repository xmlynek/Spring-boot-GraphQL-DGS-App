package com.example.sales.service.query;

import com.example.sales.datasource.repository.SalesOrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SalesOrderItemQueryService {

    private final SalesOrderItemRepository salesOrderItemRepository;

    public double findModelSalesPercentage(String modelUuidString) {
        UUID modelUuid = UUID.fromString(modelUuidString);
        double totalSales = salesOrderItemRepository.totalSalesQuantity();
        double modelSales = salesOrderItemRepository.modelSalesQuantity(modelUuid);

        double salesPercentage = (modelSales / totalSales) * 100;
        return Math.round(salesPercentage * 10d) / 10d;
    }
}
