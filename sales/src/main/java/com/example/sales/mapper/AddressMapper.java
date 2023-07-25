package com.example.sales.mapper;

import com.course.graphql.generated.types.AddressCreateRequest;
import com.example.sales.datasource.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AddressMapper {

    @Mapping(target = "uuid", ignore = true)
    Address addressRequestToEntity(AddressCreateRequest addressCreateRequest);
}
