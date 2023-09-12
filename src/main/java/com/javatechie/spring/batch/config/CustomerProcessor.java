package com.javatechie.spring.batch.config;

import com.javatechie.spring.batch.client.RuleDetailsFeignClient;
import com.javatechie.spring.batch.entity.Customer;
import com.javatechie.spring.batch.entity.OfferSegmentPermutations;
import com.javatechie.spring.batch.entity.RuleDetails;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Autowired
    private RuleDetailsFeignClient ruleDetailsFeignClient;

    RuleDetails ruleDetails;

    List<OfferSegmentPermutations> offerSegmentPermutations = new ArrayList<>();

    HashMap<String, List<Customer>> permutationCountMap = new HashMap<>();

    @BeforeStep
    private void beforeStep() {
        ruleDetails = ruleDetailsFeignClient.getRule();
        generatePermutations();
        System.out.println(offerSegmentPermutations);
    }

    @AfterStep
    private void afterStep() {
        System.out.println("after step");
        System.out.println(permutationCountMap);
    }

    private void generatePermutations() {
        ruleDetails.getOfferId().forEach(offerId -> {
            ruleDetails.getSegmentId().forEach(segmentId -> {
                OfferSegmentPermutations offerSegmentPermutation = new OfferSegmentPermutations();
                offerSegmentPermutation.setOfferId(offerId);
                offerSegmentPermutation.setSegmentId(segmentId);
                offerSegmentPermutations.add(offerSegmentPermutation);
            });
        });
    }

    @Override
    public Customer process(Customer customer) throws Exception {
        String key = customer.getOfferId() + ":" + customer.getSegmentId();
        permutationCountMap.computeIfAbsent(key, k -> new ArrayList<>());
        //Fixed
        if (ruleDetails.getSamplingType().equals("Fixed")) {
            if (permutationCountMap.get(key).size() < ruleDetails.getSamplingQty()) {
                permutationCountMap.get(key).add(customer);
                return customer;
            }
        }
        //todo Percentage
        return null;
    }
}
