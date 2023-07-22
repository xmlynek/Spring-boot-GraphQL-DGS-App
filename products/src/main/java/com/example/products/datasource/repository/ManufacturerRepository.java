package com.example.products.datasource.repository;

import com.example.products.datasource.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, UUID>,
        JpaSpecificationExecutor<Manufacturer> {
}
