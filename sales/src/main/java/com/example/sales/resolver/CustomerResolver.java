package com.example.sales.resolver;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.*;
import com.example.sales.datasource.entity.Customer;
import com.example.sales.mapper.CustomerMapper;
import com.example.sales.service.command.CustomerCommandService;
import com.example.sales.service.query.CustomerQueryService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetchingEnvironment;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

        var listCustomersQL = customersPage.stream()
                .map(customerMapper::entityToCustomerGraphQl)
                .toList();

        var customerConnection = new SimpleListConnection<>(listCustomersQL).get(env);

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

    @DgsData(
            parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.DocumentCreate
    )
    public CustomerMutationResponse addDocumentToCustomer(@InputArgument CustomerUniqueInput customer,
                                                          @InputArgument DocumentType documentType,
                                                          DataFetchingEnvironment environment) {
        var existingCustomer = customerQueryService.getUniqueCustomerFromInput(customer)
                .orElseThrow(() -> new DgsEntityNotFoundException("Customer with given uuid or email not found"));

        MultipartFile document = environment.getArgument(DgsConstants.MUTATION.DOCUMENTCREATE_INPUT_ARGUMENT.Document);

        Customer updatedCustomer = customerCommandService.addDocumentToCustomer(existingCustomer, documentType, document);

        return CustomerMutationResponse.newBuilder()
                .customerUuid(String.valueOf(updatedCustomer.getUuid()))
                .success(true)
                .message(String.format("Document %s successfully uploaded", document.getOriginalFilename()))
                .build();
    }

    @DgsData(
            parentType = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.CustomerUpdate
    )
    public CustomerMutationResponse updateCustomer(@InputArgument CustomerUniqueInput customer,
                                                   @InputArgument CustomerUpdateRequest customerUpdate) {
        var existingCustomer = customerQueryService.getUniqueCustomerFromInput(customer)
                .orElseThrow(() -> new DgsEntityNotFoundException("Customer with given uuid or email not found"));

        var updatedCustomer = customerCommandService.updateCustomer(existingCustomer, customerUpdate);

        return CustomerMutationResponse.newBuilder()
                .customerUuid(String.valueOf(updatedCustomer.getUuid()))
                .success(true)
                .message(String.format("Customer %s successfully updated!", updatedCustomer.getFullName()))
                .build();
    }

}
