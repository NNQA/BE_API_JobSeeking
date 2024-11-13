package com.quocanh.doan.Service.Interface.ClientRequest;

import com.quocanh.doan.dto.response.Job.JobResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

public interface IClientRequest {
    List<JobResponse> searchJobRequestFromClient(String title, String provinceName, String cate, String experience,
                                                 String jobType, String level, Integer salary);
    Set<String> getAllProvinceName();
    Set<String> getAllCategoryName();

    JobResponse getJobDetailsWithJob(String title);
}
