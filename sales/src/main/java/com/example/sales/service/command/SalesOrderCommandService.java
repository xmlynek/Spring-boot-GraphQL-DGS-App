package com.example.sales.service.command;

import com.course.graphql.generated.types.CreateSalesOrderInput;
import com.example.sales.datasource.entity.Customer;
import com.example.sales.datasource.entity.SalesOrder;
import com.example.sales.datasource.repository.CustomerRepository;
import com.example.sales.datasource.repository.SalesOrderRepository;
import com.example.sales.mapper.SalesOrderMapper;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@AllArgsConstructor
public class SalesOrderCommandService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;

    private final SalesOrderMapper salesOrderMapper;

    @Transactional
    public SalesOrder createSalesOrder(CreateSalesOrderInput salesOrderInput) {
        Customer customer = customerRepository.findById(UUID.fromString(salesOrderInput.getCustomerUuid()))
                .orElseThrow(() -> new DgsEntityNotFoundException(
                        String.format("Customer UUID %s not found", salesOrderInput.getCustomerUuid())
                )
        );

        SalesOrder mappedSalesOrder = salesOrderMapper.salesOrderInputToEntity(salesOrderInput);
        mappedSalesOrder.setCustomer(customer);

        return salesOrderRepository.save(mappedSalesOrder);
    }
}
