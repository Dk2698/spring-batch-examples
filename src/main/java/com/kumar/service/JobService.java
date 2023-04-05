package com.kumar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kumar.request.JobParamsRequest;

//@Service
public class JobService {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Qualifier("firstJob")
	@Autowired
	private Job firstJob;
	
	@Qualifier("secondJob")
	@Autowired
	private Job secondJob;
	
	
	
	@Async
	public void startJob(String jobName, List<JobParamsRequest> jobParamsRequestList) {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("currentTime", new JobParameter(System.currentTimeMillis()));
		
		for(JobParamsRequest  list : jobParamsRequestList) {
			params.put(list.getParamKey(), new JobParameter(list.getParamValue()));
		}
		JobParameters jobParamters = new JobParameters(params);
		
		try {
			JobExecution jobExecution = null;
			if(jobName.equals("First Job")) {
				jobExecution = jobLauncher.run(firstJob, jobParamters);
			}else if(jobName.equals("Second Job")) {
				jobExecution = jobLauncher.run(secondJob, jobParamters);
			}
			
			System.out.println("Job Excuetion Id = " + jobExecution.getId());
		} catch(Exception ex) {
			System.out.println("Exception while starting job");
		}
		
	}

}
