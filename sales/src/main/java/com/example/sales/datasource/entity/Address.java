package com.example.sales.datasource.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Address {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String street;

    private String city;

    private String zipcode;

}
