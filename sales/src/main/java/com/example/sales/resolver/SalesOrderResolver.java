package com.example.sales.resolver;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.CreateSalesOrderInput;
import com.course.graphql.generated.types.SalesOrderMutationResponse;
import com.example.sales.datasource.entity.SalesOrder;
import com.example.sales.service.command.SalesOrderCommandService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import lombok.AllArgsConstructor;

@DgsComponent
@AllArgsConstructor
public class SalesOrderResolver {

    private final SalesOrderCommandService salesOrderCommandService;

    @DgsData(
            parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.SalesOrderCreate
    )
    public SalesOrderMutationResponse createSalesOrder(@InputArgument CreateSalesOrderInput salesOrder) {
        SalesOrder createdSalesOrder = salesOrderCommandService.createSalesOrder(salesOrder);

        return SalesOrderMutationResponse.newBuilder()
                .orderNumber(createdSalesOrder.getOrderNumber())
                .salesOrderUuid(createdSalesOrder.getUuid().toString())
                .customerUuid(salesOrder.getCustomerUuid())
                .success(true)
                .message(String.format("Sales Order created with order number %s for customer %s!",
                        createdSalesOrder.getOrderNumber(), salesOrder.getCustomerUuid()))
                .build();
    }
}
