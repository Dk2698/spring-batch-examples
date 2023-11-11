package com.kumar.BatchExample.config;

import com.kumar.BatchExample.entity.Customer;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringBatchConfig {

    @Bean
    public Job jobBean(JobRepository jobRepository,
                       JobCompletionNotificationImpl listener,
                       Step steps
    ) {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(steps)
                .build();
    }

    @Bean
    public Step steps(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      ItemReader<Customer> reader,
                      ItemProcessor<Customer, Customer> processor,
                      ItemWriter<Customer> writer
    ) {
        return new StepBuilder("step", jobRepository)
                .<Customer, Customer>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    //reader
    @Bean
    public FlatFileItemReader<Customer> reader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("itemReader")
                .resource(new ClassPathResource("customers.csv"))
                .delimited()
                .names("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob")
                .linesToSkip(1)
                .targetType(Customer.class)
                .build();
    }

    @Bean
    public ItemProcessor<Customer, Customer> itemProcessor() {
        return new CustomerProcessor();
    }

    @Bean
    public ItemWriter<Customer> itemWriter(EntityManagerFactory entityManagerFactory) {

        return new JpaItemWriterBuilder<Customer>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

}
