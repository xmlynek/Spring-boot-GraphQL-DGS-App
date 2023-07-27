package com.example.sales.mapper;

import com.course.graphql.generated.types.CustomerCreateRequest;
import com.example.sales.datasource.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        uses = { AddressMapper.class, DocumentMapper.class, HelperMapper.class, SalesOrderMapper.class }
)
public interface CustomerMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "salesOrders", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Customer customerRequestToEntity(CustomerCreateRequest createRequest);

    com.course.graphql.generated.types.Customer entityToCustomerGraphQl(Customer customer);
}
