package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.Applicant;
import com.quocanh.doan.Model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    boolean existsApplicantByUser_IdAndJob_Id(Long userId, Long jobId);
    Optional<Applicant> findByUser_IdAndJob_Id(Long userId, Long jobId);
    @Query("SELECT a FROM Applicant a WHERE a.job IN :jobs")
    Page<Applicant> findApplicantsByJobs(@Param("jobs") List<Job> jobs, Pageable pageable);

    Optional<Applicant> findByIdAndAndUser_Name(Long idApp, String username);
}
