package com.javatechie.spring.batch.config;

import com.javatechie.spring.batch.client.RuleDetailsFeignClient;
import com.javatechie.spring.batch.entity.Customer;
import com.javatechie.spring.batch.entity.OfferSegmentPermutations;
import com.javatechie.spring.batch.entity.RuleDetails;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Autowired
    private RuleDetailsFeignClient ruleDetailsFeignClient;

    private List<Customer> processedCustomers = new ArrayList<>();

    RuleDetails ruleDetails;

    HashMap<String, OfferSegmentPermutations> permutationsMap = new HashMap<>();

    @BeforeStep
    private void beforeStep() {
        ruleDetails = ruleDetailsFeignClient.getRule();
        generatePermutations();
    }

    private void generatePermutations() {
        ruleDetails.getOfferId().forEach(offerId -> {
            ruleDetails.getSegmentId().forEach(segmentId -> {
                OfferSegmentPermutations offerSegmentPermutation = new OfferSegmentPermutations();
                offerSegmentPermutation.setOfferId(offerId);
                offerSegmentPermutation.setSegmentId(segmentId);
                permutationsMap.put(offerId + ":" + segmentId, offerSegmentPermutation);
            });
        });
    }

    @Override
    public Customer process(Customer customer) throws Exception {
        if (permutationsMap.containsKey(customer.getOfferId() + ":" + customer.getSegmentId())) {
            customer.setSamplingType(ruleDetails.getSamplingType());
            customer.setSamplingQty(ruleDetails.getSamplingQty());
            return customer;
        }
        return null;
    }

    @AfterStep
    //todo process all the segmentation here and store results in processedCustomers list
    private void afterStep(StepExecution stepExecution) {
        //todo sampling process & get final PROCESSED_CUSTOMERS, below is just dummy setting
        
        processedCustomers.add(new Customer());
        processedCustomers.add(new Customer());
        processedCustomers.add(new Customer());
        stepExecution.getExecutionContext().put("PROCESSED_CUSTOMERS", processedCustomers);
        System.out.println("After step");
    }

}
