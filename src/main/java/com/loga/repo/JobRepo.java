package com.loga.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.loga.entity.Job;
import com.loga.entity.Job.Status;

@Transactional
@Repository
public interface JobRepo extends JpaRepository<Job,Long> {

	@Modifying
	@Query("update Job j set j.status = :status WHERE j.id = :jobid")
    void setJobStatus(@Param("jobid") Long id, @Param("status") Status status);

}
