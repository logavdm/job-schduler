package com.loga.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class JobRunDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false,unique = true)
	private Long id;
	private String description;
	@CreationTimestamp
	private LocalDateTime createdat;
	@UpdateTimestamp
	private LocalDateTime updatedat;
	
	@ManyToOne
	@JsonBackReference
	private Job job;
	
}
