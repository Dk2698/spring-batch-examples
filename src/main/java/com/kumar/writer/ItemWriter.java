package com.kumar.writer;

import com.kumar.entity.Student;
import com.kumar.model.StudentCsv;
import com.kumar.model.StudentJdbc;
import com.kumar.model.StudentJson;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

//@Configuration
public class ItemWriter {

    @Bean
    @StepScope
    public FlatFileItemWriter<StudentJdbc> flatFileItemWriter(
            @Value("#{jobParemeters['outputFile']}") FileSystemResource fileSystemResource
            ){

        FlatFileItemWriter<StudentJdbc> flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setResource(fileSystemResource);

        flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write("Id, First Name ,Last Name, Email");
            }
        });

        flatFileItemWriter.setLineAggregator(
                new DelimitedLineAggregator<StudentJdbc>(){
                    {
                        setFieldExtractor(new BeanWrapperFieldExtractor<StudentJdbc>(){
                            {
                                setNames(new String[]{"id","firstName","lastName","email"});
                            }
                        });
                    }
                });


        flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {
            @Override
            public void writeFooter(Writer writer) throws IOException {
                writer.write("created @ " + new Date());
            }
        });
        return  flatFileItemWriter;
    }

    @StepScope
    @Bean
    public JsonFileItemWriter<StudentJson> jsonFileItemWriter(
            @Value("#{jobParemeters['outputFile']}") FileSystemResource fileSystemResource
    ){

        JsonFileItemWriter<StudentJson> jsonFileItemWriter = new JsonFileItemWriter<>(
                fileSystemResource, new JacksonJsonObjectMarshaller<StudentJson>()
        );

        return  jsonFileItemWriter;
    }
    // write into mysql
    public JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter(){
        JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();

//        jdbcBatchItemWriter.setDataSource(univercityDatascource);
        jdbcBatchItemWriter.setSql("insert into student(id, first_name, last_name, email) "+ "values(:id,:firstName,:lastName, :email)");

        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<StudentCsv>()
        );


        return  jdbcBatchItemWriter;
    }

    // jpa reader and writer and first set datasource and confidentiality manger
    public JpaCursorItemReader<Student> jpaCursorItemReader(){
        JpaCursorItemReader<Student> jpaCursorItemReader = new JpaCursorItemReader<>();

//        jpaCursorItemReader.setEntityManagerFactory(postgressqlentityMangerFactory);
        jpaCursorItemReader.setQueryString("From Student");
        return  jpaCursorItemReader;
    }

    public JpaItemWriter<com.kumar.mysql.entity.Student> jpaItemWriter(){
        JpaItemWriter<com.kumar.mysql.entity.Student> jpaItemWriter = new JpaItemWriter();
//        jpaItemWriter.setEntityManagerFactory(mysqlENtityMangerFactory);// no need sql query
        // need to traction

        return  jpaItemWriter;
    }
}
