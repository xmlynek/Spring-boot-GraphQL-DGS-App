package com.example.sales.datasource.mapper;

import com.course.graphql.generated.types.CustomerCreateRequest;
import com.example.sales.datasource.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        uses = { AddressMapper.class }
)
public interface CustomerMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "salesOrders", ignore = true)
    Customer customerRequestToEntity(CustomerCreateRequest createRequest);
}
