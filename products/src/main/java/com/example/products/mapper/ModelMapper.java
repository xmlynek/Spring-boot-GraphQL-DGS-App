package com.example.products.mapper;

import com.course.graphql.generated.types.ModelSimple;
import com.example.products.datasource.entity.Model;
import org.mapstruct.Mapper;

@Mapper
public interface ModelMapper {

    ModelSimple modelEntityToSimpleModel(Model model);
}
