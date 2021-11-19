//package com.loga.service;
//
//import java.util.Map.Entry;
//import java.util.concurrent.ScheduledFuture;
//
//import javax.annotation.PreDestroy;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.loga.config.JobRunner;
//import com.loga.repo.JobRepo;
//
//@Service
//public class JobService {
//	
//	@Autowired
//	private JobRepo jobRepo;
//	
//	@PreDestroy
//	public void preDestroy() {
//		for (Entry<Long, ScheduledFuture<?>> entry : JobRunner.runningJobs.entrySet()) {
//			entry.getValue().cancel(true);
//			jobRepo.setJobStatus(entry.getKey(), "STOPPED");
//		}
//	}
//
//}
