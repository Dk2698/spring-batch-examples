package com.kumar.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing 
@ComponentScan({"com.kumar.config", "com.kumar.service","com.kumar.listener",
	"com.kumar.reader","com.kumar.processor","com.kumar.writer","com.kumar.controller"})
@EnableAsync
@EnableScheduling
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
