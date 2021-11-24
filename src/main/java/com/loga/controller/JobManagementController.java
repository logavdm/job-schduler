package com.loga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loga.entity.Job;
import com.loga.entity.Job.Action;
import com.loga.entity.Job.Status;
import com.loga.exception.JobException;
import com.loga.models.Response;
import com.loga.repo.JobRepo;
import com.loga.service.JobService;

@RestController
@RequestMapping("/job")
public class JobManagementController {
	
	@Autowired
	private JobRepo jobRepo;
	
	@Autowired
	private JobService jobService;
	
	@PostMapping("")
	public List<Job> getJobs(){
		return jobRepo.findAll();
	}
	
	@GetMapping("/get/{id}")
	public Job getJobByID(@PathVariable("id")long id){
		return jobRepo.findById(id).get();
	}
	
	@GetMapping("/status/{status}")
	public List<Job> getJobByStatus(@PathVariable("status")Status status){
		return jobRepo.findByStatus(status);
	}
	
	@GetMapping("/action/{id}/{action}")
	public ResponseEntity<Object> jobAction(@PathVariable("id")Long id,@PathVariable("action")Action action){
		String status="success";
		try {
			
			
			jobService.jobAction(id, action);
		}catch (JobException e) {
			status=e.getMessage();
		}catch (Exception e) {
			status=e.getMessage();
		}
		return ResponseEntity.ok(status);
	}
		
}
