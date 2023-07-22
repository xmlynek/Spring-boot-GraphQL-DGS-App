package com.example.products.datasource.repository;

import com.example.products.datasource.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeriesRepository extends JpaRepository<Series, UUID>, JpaSpecificationExecutor<Series> {
}
