package com.example.sales.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "loans")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Loan {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String financeCompany;

    private String contactPersonName;

    private String contactPersonPhone;

    private String contactPersonEmail;

    @OneToOne
    @JoinColumn(name = "finance_uuid")
    private Finance finance;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
