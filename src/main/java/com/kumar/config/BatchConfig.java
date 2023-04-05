package com.kumar.config;

import com.kumar.model.StudentCsv;
import com.kumar.model.StudentJson;
import com.kumar.model.User;
import com.kumar.processor.UserItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.io.File;

@Configuration
//@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public FlatFileItemReader<User> flatFileItemReader(){
        FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();


        flatFileItemReader.setResource(new ClassPathResource("/csv/customers.csv"));

        flatFileItemReader.setLineMapper(getLineMapper());

        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;

    }

    @Bean
    public LineMapper<User> getLineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        // passing column of csv file
        lineTokenizer.setNames(new String[]{ "id","firstName","lastName","email"});
        lineTokenizer.setIncludedFields(new int[]{ 0, 1, 2,3});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public UserItemProcessor processor(){
        return  new UserItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<User> jdbcBatchItemWriter(){
        JdbcBatchItemWriter<User> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();


        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<User>()
        );
        jdbcBatchItemWriter.setSql("insert into user(id, firstName, lastName, email) "+ "values(:id,:firstName,:lastName, :email)");
        jdbcBatchItemWriter.setDataSource(dataSource);

        return  jdbcBatchItemWriter;
    }

    @Bean
    public Job importUserJob() {
        return this.jobBuilderFactory.get("USER-IMPORT-JOB")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();

    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                .<User, User>chunk(10)
                .reader(flatFileItemReader())
                .processor(processor())
                .writer(jdbcBatchItemWriter())
                .build();
    }
}
