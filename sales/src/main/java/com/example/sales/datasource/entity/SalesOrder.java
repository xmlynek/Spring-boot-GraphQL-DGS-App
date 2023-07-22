package com.example.sales.datasource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sales_orders")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SalesOrder {

    @Id
    @GeneratedValue
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "customer_uuid")
    private Customer customer;

    @CreationTimestamp
    private ZonedDateTime orderDateTime;

    @Column(unique = true)
    private String orderNumber;

    @OneToMany
    @JoinColumn(name = "sales_order_uuid")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<SalesOrderItem> salesOrderItems;

    @OneToOne(mappedBy = "salesOrder")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Finance finance;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
