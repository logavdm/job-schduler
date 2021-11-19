package com.loga.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.loga.entity.Job;
import com.loga.entity.Job.Status;
import com.loga.repo.JobRepo;
import com.loga.runnerpkg.JobRunnerInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobRunner implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private JobRepo jobRepo;

	@Autowired
	private JobSchdulerProps props;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TaskScheduler jobSchduler;

	public static Map<Long, ScheduledFuture<?>> runningJobs = new LinkedHashMap<>();

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			log.info("Schduled job configuration started....");
			int currentPage = 0;
			int offset = 0;
			int totalPage = 1;
			while (currentPage < totalPage) {
				// Get job list by pagination
				Pageable pageable = PageRequest.of(offset, props.getMaxJobPerPage());
				Page<Job> pageJobs = jobRepo.findAll(pageable);

				for (Job job : pageJobs) {

					if (!job.isEnabled()) {
						log.info("Job not enabled , {}", job);
						continue;
					}

					Class<?> clz = getRunnerClz(job.getJobClass());
					if (clz == null) {
						log.info("Defined job runner class not found  :: {}", job.getJobClass());
						job.setStatus(Status.FAILED);
						jobRepo.save(job);
						continue;
					}

					if (!JobRunnerInterface.class.isAssignableFrom(clz)) {
						log.info("Job Runner not an part of job runner :: {}", job.getJobClass());
						job.setStatus(Status.REJECTED);
						jobRepo.save(job);
						continue;
					}
					JobRunnerInterface jobInstance = (JobRunnerInterface) clz.getDeclaredConstructor().newInstance();
					applicationContext.getAutowireCapableBeanFactory().autowireBean(jobInstance);
					jobInstance.config(job);

					ScheduledFuture<?> taskItem = jobSchduler.schedule(jobInstance,new CronTrigger(job.getExpression()));
					runningJobs.put(job.getId(), taskItem);
					job.setStatus(Status.RUNNING);
					jobRepo.save(job);
				}

				// reset the offset for next page
				totalPage = pageJobs.getTotalPages();
				currentPage++;
				offset = (currentPage == 0) ? props.getMaxJobPerPage() : (props.getMaxJobPerPage() * currentPage);
			}
		} catch (Exception e) {
			log.error("Exception occured when Job Configuration... " + e.getMessage());
		}
	}

	private Class<?> getRunnerClz(String strClass) {
		Class<?> clz = null;
		try {
			clz = Class.forName(strClass);
		} catch (Exception e) {
			log.error("Job Runner Class not Found :: {}", e.getMessage());
		}
		return clz;
	}

}
