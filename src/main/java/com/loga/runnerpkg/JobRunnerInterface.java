package com.loga.runnerpkg;

import com.loga.entity.Job;

public interface JobRunnerInterface extends Runnable{
	
	public void config(Job job);
	
}
