package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobTypeRepository extends JpaRepository<JobType, Long> {

    Optional<JobType> findByJobTypeName(String jobName);
}
