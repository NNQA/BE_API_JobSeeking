package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.Company;
import com.quocanh.doan.Model.Job;
import com.quocanh.doan.Model.JobPosition;
import com.quocanh.doan.Model.JobType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    @NonNull Job getById(@NonNull Long id);
    Job getByIdAndCompany(Long id, Company company);
    @EntityGraph(attributePaths = {"categories", "skills"})
    @Query("SELECT j FROM Job j WHERE j.id = :jobId AND j.company.id = :companyId")
    Job findByIdAndCompanyWithDetails(@Param("jobId") Long jobId, @Param("companyId") Long companyId);
    @EntityGraph(attributePaths = {"categories", "skills"})
    @Query("SELECT j FROM Job j WHERE j.id = :jobId AND j.company.id = :companyId")
    Optional<Job> findByIdAndCompanyWithDetailsWithOptional(@Param("jobId") Long jobId, @Param("companyId") Long companyId);
    Job findByIdAndCompany(Long jobId, Company company);
    @Query("SELECT j FROM Job j WHERE j.title LIKE %:title%")
    Optional<Job> findByTitleContaining(@Param("title") String title);
    Optional<Job> findByTitleAndCompanyAndTypeAndPosition(String title, Company company, JobType type, JobPosition position);
    Page<Job> findAllByCompany(Company company, Pageable pageable);
    List<Job> getAllByCompany(Company company);

    List<Job> findTop9ByOrderByCreatedDateTimeDesc();
}
