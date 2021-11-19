package com.loga.runnerpkg;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.loga.entity.Job;
import com.loga.entity.JobRunDetails;
import com.loga.repo.JobRunDetailsRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoRunner implements JobRunnerInterface{
	
	private Job job;
	
	@Autowired
	private JobRunDetailsRepo jobRunRepo;


	public void config(Job job) {
		this.job=job;
	}

	@Override
	public void run() {
		log.info("Job is running ... , Job id :: {} , Job Name :: {}",job.getId(),job.getName());
		JobRunDetails jobRun=new JobRunDetails();
		jobRun.setJob(job);
		jobRun.setDescription("Job successfully run at :: "+new Date().toString());
		jobRunRepo.save(jobRun);
	}

}
