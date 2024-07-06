package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPostionRepository extends JpaRepository<JobPosition, Long> {
    Optional<JobPosition> findByJobPositionName(String name);
}
