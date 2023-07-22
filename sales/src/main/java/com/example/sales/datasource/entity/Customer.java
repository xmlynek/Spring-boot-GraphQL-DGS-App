package com.example.sales.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Column(unique = true)
    private String email;

    private LocalDate birthDate;

    private String fullName;

    private String phone;

    @OneToMany
    @JoinColumn(name = "customer_uuid")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Address> addresses;

//    @OneToMany
//    @JoinColumn(name = "customer_uuid")
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    @Fetch(FetchMode.SUBSELECT)
//    private List<CustomerDocument> documents;

    @OneToMany(mappedBy = "customer")
    @BatchSize(size = 50)
    private List<SalesOrder> salesOrders;

}
