package com.kumar.controller;



import java.util.List;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kumar.request.JobParamsRequest;
import com.kumar.service.JobService;
//
//@RestController
//@RequestMapping("/api/job")
public class JobController {
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private JobOperator jobOperator;
	
	
	@GetMapping("/start/{jobName}")
	public String startJob(@PathVariable String jobName,
			@RequestBody List<JobParamsRequest> jobParamsRequestList
			) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		jobService.startJob(jobName, jobParamsRequestList);
		
		return "start job ....";
	}
	
	@GetMapping("/start/")
	public String start() {
		
		return "start job ....";
	}
	
	@GetMapping("/stop/{jobExeutionId}")
	public String stopJob(@PathVariable long jobExeutionId) {
		
		try {
			jobOperator.stop(jobExeutionId);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return "stop job ....";
	}
	
	
}
