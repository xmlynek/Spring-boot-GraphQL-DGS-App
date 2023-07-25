package com.example.sales.service.command;

import com.course.graphql.generated.types.AddressCreateRequest;
import com.course.graphql.generated.types.CustomerCreateRequest;
import com.course.graphql.generated.types.DocumentType;
import com.example.sales.datasource.entity.Address;
import com.example.sales.datasource.entity.Customer;
import com.example.sales.datasource.entity.CustomerDocument;
import com.example.sales.mapper.AddressMapper;
import com.example.sales.mapper.CustomerMapper;
import com.example.sales.datasource.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerCommandService {

    private final CustomerRepository customerRepository;

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

    public Customer addDocumentToCustomer(Customer customer, DocumentType documentType, MultipartFile document) {
        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setDocumentType(documentType);

        // pretend process upload, e.g. to S3 bucket or other storage
        var uploadedDocumentPath = String.format("%s/%s/%s-%s-%s",
                "https://dummy-storage.com", customer.getUuid(), documentType,
                RandomStringUtils.randomAlphabetic(6).toLowerCase(),
                document.getOriginalFilename());

        customerDocument.setDocumentPath(uploadedDocumentPath);

        customer.getDocuments().add(customerDocument);
        return customerRepository.save(customer);
    }
}
