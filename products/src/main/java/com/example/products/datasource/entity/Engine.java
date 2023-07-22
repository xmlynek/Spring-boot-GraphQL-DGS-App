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
@Table(name = "engines")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Engine {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String description;
    private int horsePower;
    private int torque;
    private int capacityCc;
}
