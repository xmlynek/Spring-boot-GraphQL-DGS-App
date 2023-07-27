package com.example.sales.mapper;

import com.course.graphql.generated.types.SalesOrderItemInput;
import com.example.sales.datasource.entity.SalesOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = { HelperMapper.class })
public interface SalesOrderItemMapper {

    @Mapping(target = "uuid", ignore = true)
    SalesOrderItem salesOrderItemInputToEntity(SalesOrderItemInput salesOrderItemInput);

    @Mapping(target = "notes", ignore = true)
    com.course.graphql.generated.types.SalesOrderItem salesOrderItemToGraphQL(SalesOrderItem salesOrderItem);
}
