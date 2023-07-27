package com.example.sales.mapper;

import com.course.graphql.generated.types.CreateSalesOrderInput;
import com.example.sales.datasource.entity.SalesOrder;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = { HelperMapper.class, SalesOrderItemMapper.class, FinanceMapper.class })
public interface SalesOrderMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "orderDateTime", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "orderNumber", expression = "java(\"SALES-\"+org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(8).toUpperCase())")
    SalesOrder salesOrderInputToEntity(CreateSalesOrderInput salesOrderInput);

    @AfterMapping
    default void afterEntityMapping(@MappingTarget SalesOrder salesOrder) {
        salesOrder.getFinance().setSalesOrder(salesOrder);
    }

    com.course.graphql.generated.types.SalesOrder salesOrderToGraphQL(SalesOrder salesOrder);
}
