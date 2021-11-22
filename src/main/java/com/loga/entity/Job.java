package com.loga.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Job {
	
	public enum Status {
	    RUNNING, PAUSED, STOPPED,FAILED,REJECTED
	}
	
	public enum Action {
	    START, STOP
	}
			
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, unique = true)
	private long id;
	private String name;
	private String expression;
	private boolean enabled;
	private String jobClass;
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@CreationTimestamp
	private LocalDateTime createdat;
	@UpdateTimestamp
	private LocalDateTime updatedat;
	
	private boolean jobRunLogEnabled;
	@OneToMany(mappedBy = "job")
	@JsonManagedReference
	private List<JobRunDetails> jobRunDetails;

	@Override
	public String toString() {
		return " Job ID :: " + id + " , Job Name :: " + name + " , Cron Expression :: " + expression + " , Enabled :: "
				+ enabled + " ,Status :: " + status + " , CreatedAt :: " + createdat + " , UpdatedAt :: " + updatedat;
	}
	
	@Override
	public int hashCode() {
		return (int) id;
	}
}
