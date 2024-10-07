package com.quocanh.doan.Service.Interface.Job;

import com.quocanh.doan.Model.Job;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.dto.request.Job.JobRequest;
import com.quocanh.doan.dto.response.Job.JobPaginationResponse;
import com.quocanh.doan.dto.response.Job.JobTypeResponse;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IJob {
    void addNewJob(JobRequest request, UserPrincipal userPrincipal, BindingResult result);
    List<Job> getAllJob();

    Job getById(Long id);

    JobPaginationResponse getAllJobPage(Long id, Integer pageNo, Integer pageSize);
}
