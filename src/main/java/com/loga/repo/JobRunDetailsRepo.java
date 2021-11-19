package com.loga.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loga.entity.JobRunDetails;

@Transactional
@Repository
public interface JobRunDetailsRepo extends JpaRepository<JobRunDetails,Long> {

}
