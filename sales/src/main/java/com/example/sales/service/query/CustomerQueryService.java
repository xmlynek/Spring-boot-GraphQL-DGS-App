package com.example.sales.service.query;

import com.course.graphql.generated.types.CustomerUniqueInput;
import com.example.sales.datasource.entity.Customer;
import com.example.sales.datasource.repository.CustomerRepository;
import com.example.sales.datasource.specification.CustomerSpecification;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerQueryService {

    private final CustomerRepository customerRepository;

    public Page<Customer> findCustomers(Optional<CustomerUniqueInput> customer, Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(50),
                Sort.by(CustomerSpecification.FIELD_EMAIL)
        );

        if (customer.isPresent()) {
            return getUniqueCustomerFromInput(customer.get())
                    .map(customerResponse -> new PageImpl<Customer>(List.of(customerResponse), pageRequest, 1))
                    .orElse(new PageImpl<Customer>(new ArrayList<>(), pageRequest, 0));
        }

        return customerRepository.findAll(pageRequest);
    }

    public Optional<Customer> getUniqueCustomerFromInput(CustomerUniqueInput customerUniqueInput) {
        if (StringUtils.isNoneBlank(customerUniqueInput.getUuid(), customerUniqueInput.getEmail())) {
            throw new IllegalArgumentException("Only one parameter in CustomerUniqueInput is allowed, not both");
        } else if (StringUtils.isAllBlank(customerUniqueInput.getUuid(), customerUniqueInput.getEmail())) {
            throw new IllegalArgumentException("One of the customer UUID or email must exist");
        }

        var specification = StringUtils.isNotBlank(customerUniqueInput.getEmail()) ?
                CustomerSpecification.emailEqualsIgnoreCase(customerUniqueInput.getEmail()) :
                CustomerSpecification.uuidEqualsIgnoreCase(customerUniqueInput.getUuid());

        return customerRepository.findOne(specification);
    }
}
