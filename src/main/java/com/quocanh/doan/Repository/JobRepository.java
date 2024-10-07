package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.Company;
import com.quocanh.doan.Model.Job;
import com.quocanh.doan.Model.JobPosition;
import com.quocanh.doan.Model.JobType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    @NonNull Job getById(@NonNull Long id);
    Optional<Job> findByTitleAndCompanyAndTypeAndPosition(String title, Company company, JobType type, JobPosition position);

    Page<Job> findAllByCompany(Company company, Pageable pageable);
}
