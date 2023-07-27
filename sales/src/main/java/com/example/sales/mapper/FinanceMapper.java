package com.example.sales.mapper;

import com.course.graphql.generated.types.FinanceInput;
import com.example.sales.datasource.entity.Finance;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = { HelperMapper.class, LoanMapper.class })
public interface FinanceMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "salesOrder", ignore = true)
    Finance financeInputToEntity(FinanceInput financeInput);

    @AfterMapping
    default void afterEntityMapping(@MappingTarget Finance finance) {
        if (finance.getIsLoan()) {
            finance.getLoan().setFinance(finance);
        }
    }

    @Mapping(target = "loan", conditionExpression = "java(finance.getIsLoan())")
    com.course.graphql.generated.types.Finance financeToGraphQL(Finance finance);
}
