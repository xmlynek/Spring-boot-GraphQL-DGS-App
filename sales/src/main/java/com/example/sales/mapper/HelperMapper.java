package com.example.sales.mapper;

import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper
public interface HelperMapper {

    default String uuidToString(UUID uuid) {
        return uuid.toString();
    }
}
