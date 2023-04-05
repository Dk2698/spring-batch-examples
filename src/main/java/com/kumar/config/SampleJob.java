package com.kumar.config;

import java.io.File;

import com.kumar.model.StudentJdbc;
import com.kumar.service.StudentService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.kumar.listener.FirstJobListener;
import com.kumar.listener.FirstStepListener;
import com.kumar.model.StudentCsv;
import com.kumar.model.StudentJson;
import com.kumar.processor.FirstItemProcessor;
import com.kumar.reader.FirstItemReader;
import com.kumar.service.SecondTasklet;
import com.kumar.writer.FirstItemWriter;
import com.kumar.writer.FlatFileWriter;

import javax.sql.DataSource;

@Configuration
public class SampleJob {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private SecondTasklet secondTasklet;
	
	@Autowired
	private FirstJobListener firstJobListener;
	
	@Autowired
	private FirstStepListener firstStepListener;
	
	@Autowired
	private FirstItemReader firstItemReader;
	
	@Autowired
	private FirstItemProcessor firstItemProcessor;
	
	@Autowired
	private FirstItemWriter firstItemWriter;
	
	@Autowired
	private FlatFileWriter flatFileWriter;


//	@Autowired
//	private DataSource dataSource;


	
//	@Bean
	public Job firstJob() {
		return jobBuilderFactory.get("First Job")
				.incrementer(new RunIdIncrementer())
				.start(firstStep())
				.next(secondStep())
				.listener(firstJobListener)
				.build();
		
	}
	
	private Step firstStep() {
		return stepBuilderFactory.get("First step")
				.tasklet(firstTask())
				.listener(firstStepListener)
				.build();
	}
	
	private Tasklet firstTask() {
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is first tasklet step");
				System.out.println("Sec = "+ chunkContext.getStepContext().getStepExecution());
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	private Step secondStep() {
		return stepBuilderFactory.get("Second step")
				.tasklet(secondTasklet)
				.build();
	}
	
//	private Tasklet secondTask() {
//		return new Tasklet() {
//			
//			@Override
//			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//				System.out.println("This is second tasklet step");
//				return RepeatStatus.FINISHED;
//			}
//		};
//	}
	
//	@Bean
	public Job secondJob() {
		return jobBuilderFactory.get("Second Job")
				.incrementer(new RunIdIncrementer())
				.start(firstChunkStep())
				//.next(secondStep())
				.build();
		
	}
	

	private Step firstChunkStep() {
		return stepBuilderFactory.get("first Chunk step")
				.<Integer, Long>chunk(3)
				.reader(firstItemReader)
				.processor(firstItemProcessor)
				.writer(firstItemWriter)
				.build(); 
	}
	
	@Bean
	public Job chunkJob() {
		return jobBuilderFactory.get("Chunk Job")
				.incrementer(new RunIdIncrementer())
				.start(chunkStep1())
				.build();
		
	}
	
//	private Step chunkStep() {
//		return stepBuilderFactory.get("Chunk step")
//				.<StudentCsv, StudentCsv>chunk(3)
//				.reader(flatFileItemReader(null))
////				.processor(firstItemProcessor)
//				.writer(flatFileWriter)
//				.build(); 
//	}
	
	private Step chunkStep1() {
		return stepBuilderFactory.get("Chunk step")
				.<StudentJson, StudentJson>chunk(3)
				.reader(jsonItemReader(null))
				.writer(flatFileWriter)
				.build(); 
	}
//	public FlatFileItemReader<StudentCsv> flatFileItemReader(){
//		FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<StudentCsv>();
//		
//		
//		flatFileItemReader.setResource(new FileSystemResource(
//				new File("D:\\spring\\spring-batch\\inputFiles\\student.csv")
//				));
//		
//		flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
//			{
//				setLineTokenizer(new DelimitedLineTokenizer() {
//					{
//						setNames("ID","First Name","Last Name", "Email");
//					}
//				});
//				
//				setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
//					{
//						setTargetType(StudentCsv.class);
//					}
//				});
//				
//			}
//			
//			
//		});
//		
//		flatFileItemReader.setLinesToSkip(1);
//		
//		return flatFileItemReader;
//		
//	}
	
	// pass file as paramters
	
	@StepScope
	@Bean
	public FlatFileItemReader<StudentCsv> flatFileItemReader(
			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource
			){
		FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<StudentCsv>();
		
		
//		flatFileItemReader.setResource(new FileSystemResource(
//				new File("D:\\spring\\spring-batch\\inputFiles\\student.csv")
//				));
		flatFileItemReader.setResource(fileSystemResource);
		
		flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("ID","First Name","Last Name", "Email");
					}
				});
				
				setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
					{
						setTargetType(StudentCsv.class);
					}
				});
				
			}
			
			
		});
		
		flatFileItemReader.setLinesToSkip(1);
		
		return flatFileItemReader;
		
	}
	
	public FlatFileItemReader<StudentCsv> flatFileItemReader1(){
	FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<StudentCsv>();
	
	
	flatFileItemReader.setResource(new FileSystemResource(
			new File("D:\\spring\\spring-batch\\inputFiles\\student.csv")
			));
	
	DefaultLineMapper<StudentCsv> defaultLineMapper = new DefaultLineMapper<StudentCsv>();
	
	DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
	delimitedLineTokenizer.setNames("ID", "First Name","Last Name","Email");
	
	defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
	
	BeanWrapperFieldSetMapper<StudentCsv> fieldSetMapper = new BeanWrapperFieldSetMapper<StudentCsv>();
	fieldSetMapper.setTargetType(StudentCsv.class);
	
	defaultLineMapper.setFieldSetMapper(fieldSetMapper);
	
	flatFileItemReader.setLineMapper(defaultLineMapper);
	
	
	flatFileItemReader.setLinesToSkip(1);
	
	return flatFileItemReader;
	
}
	
	
	@StepScope
	@Bean
	public JsonItemReader<StudentJson> jsonItemReader(
			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource
			) {
	  JsonItemReader<StudentJson> jsonItemReader = new JsonItemReader<>();
	  
	  jsonItemReader.setResource(fileSystemResource);
	  jsonItemReader.setJsonObjectReader(
			  new JacksonJsonObjectReader<>(StudentJson.class)
			  );

//	  jsonItemReader.setMaxItemCount(8);
//	  jsonItemReader.setCurrentItemCount(2);
	  
	  return jsonItemReader;
	}


//	@Autowired
//	@Qualifier("datasource")
//	private DataSource dataSource;
//
//	@Autowired
//	@Qualifier("universitydataSource")
//	private DataSource universitydataSource;

	public JdbcCursorItemReader<StudentJdbc> jdbcJdbcCursorItemReader(){
		JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader = new JdbcCursorItemReader<>();

//		jdbcCursorItemReader.setDataSource(universitydataSource());
//		jdbcCursorItemReader.setDataSource(universitydataSource);

		jdbcCursorItemReader.setSql("select id, first_name as firstName, last_name as LastName,"+"email");
		jdbcCursorItemReader.setRowMapper(
				new BeanPropertyRowMapper<StudentJdbc>() {
					{
						setMappedClass(StudentJdbc.class);
					}
				});

		return  jdbcCursorItemReader;
	}

//	@Autowired
//	private StudentService studentService;
//
//	public ItemReaderAdapter<StudentJson> itemReaderAdapter(){
//
//		ItemReaderAdapter<StudentJson>  itemReaderAdapter= new ItemReaderAdapter<StudentJson>();
//
//		itemReaderAdapter.setTargetObject(studentService) ; // studentservivc
//		itemReaderAdapter.setTargetMethod("getStudent");
//		itemReaderAdapter.setArguments(new Object[]{1,"Test"});
//
//		return  itemReaderAdapter;
//	}
}
