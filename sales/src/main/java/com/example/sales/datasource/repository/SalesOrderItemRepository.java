package com.example.sales.datasource.repository;

import com.example.sales.datasource.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, UUID> {

    @Query(value = "SELECT COALESCE(SUM(soi.quantity), 0) FROM SalesOrderItem soi")
    double totalSalesQuantity();

    @Query(value = "SELECT COALESCE(SUM(soi.quantity), 0) FROM SalesOrderItem soi " +
            "WHERE soi.modelUuid = :modelUuid")
    double modelSalesQuantity(@Param("modelUuid") UUID modelUuid);
}
