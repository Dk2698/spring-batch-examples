package com.kumar.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FirstStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		System.out.println("Before step "+ stepExecution.getStepName());
		System.out.println("Job Parans"+ stepExecution.getJobParameters());
		System.out.println("Job Exec context "+ stepExecution.getExecutionContext());
		
		stepExecution.getExecutionContext().put("sec", "sec value");
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		System.out.println("After step "+ stepExecution.getStepName());
		System.out.println("Job Parans"+ stepExecution.getJobParameters());
		System.out.println("Job Exec context "+ stepExecution.getExecutionContext());
		return null;
		
	}

}
