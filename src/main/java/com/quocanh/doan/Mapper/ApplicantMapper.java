package com.quocanh.doan.Mapper;

import com.quocanh.doan.Model.Applicant;
import com.quocanh.doan.Model.Job;
import com.quocanh.doan.dto.response.ApplicantResponse.ApplicantResponse;
import com.quocanh.doan.dto.response.Job.JobResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ApplicantMapper {

    ApplicantMapper INSTANCE = Mappers.getMapper(ApplicantMapper.class);

    ApplicantResponse applicantToResponse(Applicant applicant);
    List<ApplicantResponse> applicantListToAppicantResponseList(List<Applicant> applicants);
}
