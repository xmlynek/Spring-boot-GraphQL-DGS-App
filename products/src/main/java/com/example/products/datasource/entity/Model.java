package com.example.products.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "models")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Model {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;
    private double onTheRoadPrice;
    private int lengthMm;
    private int widthMm;
    private int heightMm;
    private String exteriorColor;
    private String interiorColor;
    private int releaseYear;
    private String transmission;
    private String bodyType;
    private String fuel;
    private int doors;
    private int airbags;
    private boolean isAvailable;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_uuid")
    private Engine engine;

    @OneToMany
    @JoinColumn(name = "model_uuid")
    @Fetch(FetchMode.SUBSELECT)
    private List<Feature> features;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_uuid")
    private Series series;

}
