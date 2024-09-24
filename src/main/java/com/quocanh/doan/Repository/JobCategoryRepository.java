package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.JobCategory;
import com.quocanh.doan.Model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    @Query("select s from JobCategory  s where s.jobCategoryName in :nameJobCategoryRequest")
    List<JobCategory> findByJobCategoryName(@Param("nameJobCategoryRequest") Set<String> nameJobCategoryRequest);

}
