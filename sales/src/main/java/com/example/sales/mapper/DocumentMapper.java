package com.example.sales.mapper;

import com.course.graphql.generated.types.Document;
import com.example.sales.datasource.entity.CustomerDocument;
import org.mapstruct.Mapper;

@Mapper(uses = { HelperMapper.class })
public interface DocumentMapper {

    Document entityToDocumentGraphQL(CustomerDocument customerDocument);
}
