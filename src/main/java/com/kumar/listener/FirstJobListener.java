package com.kumar.listener;


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FirstJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		System.out.println("Before Job "+ jobExecution.getJobInstance().getJobName());
		System.out.println("Job Parans"+ jobExecution.getJobParameters());
		System.out.println("Job Exec context "+ jobExecution.getExecutionContext());
		jobExecution.getExecutionContext().put("job1", "job value");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		System.out.println("After Job "+ jobExecution.getJobInstance().getJobName());
		System.out.println("Job Parans"+ jobExecution.getJobParameters());
		System.out.println("Job Exec context "+ jobExecution.getExecutionContext());
	}

}
