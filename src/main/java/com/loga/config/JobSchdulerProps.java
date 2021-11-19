package com.loga.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "job.schduler.config")
public class JobSchdulerProps {
	
	private int maxJobPerPage;

}
