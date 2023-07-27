package com.example.sales.mapper;

import com.course.graphql.generated.types.LoanInput;
import com.example.sales.datasource.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = { HelperMapper.class })
public interface LoanMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "finance", ignore = true)
    Loan loanInputToEntity(LoanInput loanInput);

    com.course.graphql.generated.types.Loan loanToGraphQL(Loan loan);
}
