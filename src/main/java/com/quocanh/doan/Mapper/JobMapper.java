package com.quocanh.doan.Mapper;

import com.quocanh.doan.Model.Job;
import com.quocanh.doan.dto.response.Job.JobResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    JobResponse jobToJobResponse(Job job);
    List<JobResponse> jobListToJobResponseList(List<Job> jobs);
}
