package com.example.products.datasource.entity;

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
@Table(name = "features")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Feature {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;
    private boolean activeByDefault;
    private boolean activeByRequest;
    private int installationPrice;
    private boolean isSafety;
    private boolean isEntertainment;
    private boolean isPerformance;
    private boolean isConvenience;
    private boolean isDisplay;

}
