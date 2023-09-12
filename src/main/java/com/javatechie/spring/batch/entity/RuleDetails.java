package com.javatechie.spring.batch.entity;

import lombok.Data;

import java.util.List;

@Data
public class RuleDetails {
    private List<String> offerId;

    private List<String> segmentId;

    private String samplingType;

    private int samplingQty;
    
}
