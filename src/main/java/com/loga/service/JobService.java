package com.loga.service;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.loga.config.JobRunner;
import com.loga.entity.Job;
import com.loga.entity.Job.Action;
import com.loga.entity.Job.Status;
import com.loga.handler.JobException;
import com.loga.repo.JobRepo;
import com.loga.runnerpkg.JobRunnerInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobService {

	@Autowired
	private JobRepo jobRepo;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TaskScheduler jobSchduler;

	public void jobAction(Long id, Action action) {
		switch (action) {
		case START:
			startJob(id);
			break;
		case STOP:
			stopJob(id);
			break;
		default:
			log.info("Invalid action  :: {}", action);
			break;
		}
	}

	public void startJob(Long id) {
		try {
			if (JobRunner.runningJobs.containsKey(id)) {
				log.info("Job already running");
				throw new JobException("job Already Running");
			}
			Optional<Job> optJob = jobRepo.findById(id);
			if (!optJob.isPresent()) {
				log.info("Invalid job");
				throw new JobException("Invalid job");
			}
			Job job = optJob.get();
			if (!job.isEnabled()) {
				log.info("Job not enabled , {}", job);
				throw new JobException("job is not enabled");
			}
			Class<?> clz = getRunnerClz(job.getJobClass());
			if (clz == null) {
				log.info("Defined job runner class not found  :: {}", job.getJobClass());
				job.setStatus(Status.FAILED);
				jobRepo.save(job);
				throw new JobException("Defined job runner class not found  :: " + job.getJobClass());
			}
			if (!JobRunnerInterface.class.isAssignableFrom(clz)) {
				log.info("Job Runner not an part of job runner :: {}", job.getJobClass());
				job.setStatus(Status.REJECTED);
				jobRepo.save(job);
				throw new JobException("Job Runner not an part of job runner :: " + job.getJobClass());
			}
			JobRunnerInterface jobInstance = (JobRunnerInterface) clz.getDeclaredConstructor().newInstance();
			applicationContext.getAutowireCapableBeanFactory().autowireBean(jobInstance);
			jobInstance.config(job);

			ScheduledFuture<?> taskItem = jobSchduler.schedule(jobInstance, new CronTrigger(job.getExpression()));
			JobRunner.runningJobs.put(job.getId(), taskItem);
			job.setStatus(Status.RUNNING);
			jobRepo.save(job);
		} catch (JobException e) {
			throw e;
		} catch (Exception e) {
			log.error("Exception occured when starting job :: {}", e.getMessage());
			throw new JobException("Job Start Exception :: " + e.getMessage());
		}
	}

	public void stopJob(Long id) {
		try {
			if (!jobRepo.existsById(id)) {
				log.info("Invalid job");
				throw new JobException("Invalid job");
			}
			if (!JobRunner.runningJobs.containsKey(id)) {
				log.info("Job Not Running");
				throw new JobException("Job Not Running");
			}
			JobRunner.runningJobs.get(id).cancel(true);
			JobRunner.runningJobs.remove(id);
			jobRepo.setJobStatus(id, Status.STOPPED);
		}catch (JobException e) {
			throw e;
		} catch (Exception e) {
			log.error("Exception occured when stop the job :: {}", e.getMessage());
			throw new JobException("Job stop Exception :: " + e.getMessage());
		}
	}

	public Class<?> getRunnerClz(String strClass) {
		Class<?> clz = null;
		try {
			clz = Class.forName(strClass);
		} catch (Exception e) {
			log.error("Job Runner Class not Found :: {}", e.getMessage());
		}
		return clz;
	}

}
