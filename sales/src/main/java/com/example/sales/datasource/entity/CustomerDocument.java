package com.example.sales.datasource.entity;

import com.course.graphql.generated.types.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "customer_documents")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerDocument {

    @Id
    @GeneratedValue
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String documentPath;

    public String getUuid() {
        return uuid.toString();
    }

}
