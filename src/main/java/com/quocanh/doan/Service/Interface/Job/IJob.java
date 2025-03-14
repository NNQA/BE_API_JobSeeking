package com.quocanh.doan.Service.Interface.Job;

import com.quocanh.doan.Model.Job;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.dto.request.Job.JobRequest;
import com.quocanh.doan.dto.response.Job.JobPaginationResponse;
import com.quocanh.doan.dto.response.Job.JobResponse;
import com.quocanh.doan.dto.response.Job.JobTypeResponse;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface IJob {
    void addNewJob(JobRequest request, UserPrincipal userPrincipal, BindingResult result);
    void updateJob(Long id,JobRequest request,  UserPrincipal userPrincipal, BindingResult result);
    List<Job> getAllJob();
    Job getById(Long id);
    JobResponse getByIdForCompany(Long id, Long idCompany);
    JobPaginationResponse getAllJobPage(Long id, Integer pageNo, Integer pageSize);
    void DeleteById(Long id);
    void approvedJob(Long id);
}
