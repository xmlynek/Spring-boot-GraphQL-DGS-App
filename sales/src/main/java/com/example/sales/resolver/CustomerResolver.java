package com.example.sales.resolver;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.*;
import com.example.sales.datasource.entity.Customer;
import com.example.sales.datasource.mapper.CustomerMapper;
import com.example.sales.service.command.CustomerCommandService;
import com.example.sales.service.query.CustomerQueryService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@DgsComponent
@AllArgsConstructor
public class CustomerResolver {

    private final CustomerQueryService customerQueryService;
    private final CustomerCommandService customerCommandService;

    private final CustomerMapper customerMapper;

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.Customer
    )
    public CustomerPagination getCustomer(DataFetchingEnvironment env,
                                          @InputArgument("customer") Optional<CustomerUniqueInput> customer,
                                          @InputArgument Integer page,
                                          @InputArgument Integer size) {
        var customersPage = customerQueryService.findCustomers(customer, page, size);

        // TODO: map customers to graphqlEntity

        var customerConnection = new SimpleListConnection<>(customersPage.getContent()).get(env);

        return CustomerPagination.newBuilder()
                .customerConnection(customerConnection)
                .page(customersPage.getNumber())
                .size(customersPage.getSize())
                .totalPages(customersPage.getTotalPages())
                .totalElement(customersPage.getTotalElements())
                .build();
    }

    @DgsData(
            parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.CustomerCreate
    )
    public CustomerMutationResponse createCustomer(@InputArgument CustomerCreateRequest customer) {
        var createdCustomer = customerCommandService.createCustomer(customer);

        return CustomerMutationResponse.newBuilder()
                .customerUuid(String.valueOf(createdCustomer.getUuid()))
                .success(true)
                .message(String.format("User %s successfully created", createdCustomer.getFullName()))
                .build();
    }

    @DgsData(
            parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.AddressCreate
    )
    public CustomerMutationResponse addAddressToCustomer(@InputArgument CustomerUniqueInput customer,
                                                         @InputArgument List<AddressCreateRequest> addresses) {
        var existingCustomer = customerQueryService.getUniqueCustomerFromInput(customer)
                .orElseThrow(() -> new DgsEntityNotFoundException("Customer with given uuid or email not found"));

        Customer updatedCustomer = customerCommandService.addAddressToCustomer(existingCustomer, addresses);

        return CustomerMutationResponse.newBuilder()
                .customerUuid(String.valueOf(updatedCustomer.getUuid()))
                .success(true)
                .message(String.format("Addresses successfully created and added to user %s", updatedCustomer.getFullName()))
                .build();
    }

}
