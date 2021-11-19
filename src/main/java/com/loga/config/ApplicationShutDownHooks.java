package com.loga.config;

import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.loga.entity.Job.Status;
import com.loga.repo.JobRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationShutDownHooks implements ApplicationListener<ContextClosedEvent> {

	@Autowired
	private JobRepo jobRepo;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		try {
			for (Entry<Long, ScheduledFuture<?>> entry : JobRunner.runningJobs.entrySet()) {
				entry.getValue().cancel(true);
				jobRepo.setJobStatus(entry.getKey(), Status.STOPPED);
				log.info("Job :: {} , updated..",entry.getKey());
			}
		} catch (Exception e) {
			log.error("Exception occured when application running job closing :: {}", e.getMessage());
		}
	}
}
