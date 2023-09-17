package com.javatechie.spring.batch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "CUSTOMERS_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private UUID uuid;

    @Column
    private String offerId;

    @Column
    private String segmentId;

    @Column
    private String transSubGrpId;

    @Column
    private String currency;

    private String samplingType;

    private int samplingQty;

}
