package com.quocanh.doan.Service.Interface.Job;

import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.dto.request.Job.JobRequest;
import org.springframework.validation.BindingResult;

public interface IJob {
    void addNewJob(JobRequest request, UserPrincipal userPrincipal, BindingResult result);
}
