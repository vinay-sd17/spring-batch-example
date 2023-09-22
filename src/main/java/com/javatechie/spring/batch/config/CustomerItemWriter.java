package com.javatechie.spring.batch.config;

import com.javatechie.spring.batch.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

@Slf4j
public class CustomerItemWriter implements ItemWriter<Customer>, Closeable {
    FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();

    private List<Customer> processedCustomerList;

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
        System.out.println(
            "do not write anything, this is just a dummy writer, write the custom list from predestroy method");
    }

    @PreDestroy
    @Override
    // todo Perform your write here not on write method
    public void close() throws IOException {
        // write the processedCustomerList
        writer.close();
    }


    @AfterStep
    public void afterWrite(StepExecution stepExecution) {
        System.out.println("After write at CustomWriter");
        processedCustomerList = (List<Customer>) stepExecution.getExecutionContext().get(
            "PROCESSED_CUSTOMERS");
    }
}
