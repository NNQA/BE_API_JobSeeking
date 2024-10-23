package com.quocanh.doan.Service.Interface.ClientRequest;

import com.quocanh.doan.dto.response.Job.JobResponse;

import java.util.List;

public interface IClientRequest {
    List<JobResponse> searchJobRequestFromClient(String title);
}