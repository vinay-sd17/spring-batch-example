package com.javatechie.spring.batch.config;

import com.javatechie.spring.batch.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class CustomerItemWriter implements ItemWriter<Customer>, Closeable {
    FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();

    HashMap<String, List<Customer>> customerHashMap = new HashMap<>();

    private List<Customer> customerList = new ArrayList<>();

    public CustomerItemWriter() {
        Resource outputResource = new FileSystemResource(
            "/Users/desika.srinivasan/Projects/spring-batch-example/src/main/resources/outputData.csv");
        writer.setResource(outputResource);
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<Customer>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Customer>() {
                    {
                        setNames(new String[]{"uuid", "offerId", "segmentId", "transSubGrpId", "currency"});
                    }
                });
            }
        });
    }

    @Override
    public void write(final List<? extends Customer> items) throws Exception {
        System.out.println(items.size());
        customerList.addAll(items);
    }

    @PreDestroy
    @Override
    // todo Perform all your action here
    public void close() throws IOException {
        System.out.println(customerList.size());
        for (Customer customer : customerList) {
            String key = customer.getOfferId() + ":" + customer.getSegmentId();
            if (customerHashMap.containsKey(key)) {
                customerHashMap.get(key).add(customer);
            } else {
                List<Customer> customerList = new ArrayList<>();
                customerList.add(customer);
                customerHashMap.put(key, customerList);
            }
        }

        writer.close();
    }
}
