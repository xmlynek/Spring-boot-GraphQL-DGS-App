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
@Table(name = "series")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Series {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;

    @OneToMany
    @JoinColumn(name = "series_uuid")
    @BatchSize(size = 50)
    private List<Characteristic> characteristics;

    @OneToMany(mappedBy = "series")
    @BatchSize(size = 50)
    private List<Model> models;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_uuid")
    private Manufacturer manufacturer;

}
