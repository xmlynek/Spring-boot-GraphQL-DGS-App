package com.example.sales.service.command;

import com.course.graphql.generated.types.AddressCreateRequest;
import com.course.graphql.generated.types.CustomerCreateRequest;
import com.course.graphql.generated.types.CustomerUniqueInput;
import com.example.sales.datasource.entity.Address;
import com.example.sales.datasource.entity.Customer;
import com.example.sales.datasource.mapper.AddressMapper;
import com.example.sales.datasource.mapper.CustomerMapper;
import com.example.sales.datasource.repository.CustomerRepository;
import com.example.sales.service.query.CustomerQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerCommandService {

    private final CustomerRepository customerRepository;
    private final CustomerQueryService customerQueryService;

    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

    public Customer createCustomer(CustomerCreateRequest customerCreateRequest) {
        return customerRepository.save(customerMapper.customerRequestToEntity(customerCreateRequest));
    }

    public Customer addAddressToCustomer(Customer customer, List<AddressCreateRequest> addresses) {

        List<Address> convertedAddresses = addresses.stream()
                .map(addressMapper::addressRequestToEntity)
                .toList();

        customer.getAddresses().addAll(convertedAddresses);

        return customerRepository.save(customer);
    }
}
