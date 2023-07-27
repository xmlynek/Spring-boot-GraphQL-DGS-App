package com.example.sales.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.UUID;

@Entity
@Table(name = "finances")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Finance {

    @Id
    @GeneratedValue
    private UUID uuid;

    private double baseAmount;

    private double taxAmount;

    private double discountAmount;

    private boolean isLoan;

    @OneToOne
    @JoinColumn(name = "sales_order_uuid")
    private SalesOrder salesOrder;

    @OneToOne(mappedBy = "finance")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Loan loan;

    public void setIsLoan(boolean isLoan) {
        this.isLoan = isLoan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public boolean getIsLoan() {
        return this.isLoan;
    }

    public Loan getLoan() {
        return this.loan;
    }

}
