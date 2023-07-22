package com.example.products.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "manufacturers")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Manufacturer {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;
    private String originCountry;
    private String description;

    @OneToMany(mappedBy = "manufacturer")
    @BatchSize(size = 50)
    private List<Series> series;

}
