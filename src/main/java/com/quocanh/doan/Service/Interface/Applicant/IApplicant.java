package com.quocanh.doan.Service.Interface.Applicant;

import com.quocanh.doan.dto.request.ApplicantRequest.ApplicantRequest;
import com.quocanh.doan.dto.response.ApplicantResponse.ApplicantPaginationResponse;
import com.quocanh.doan.dto.response.ApplicantResponse.ApplicantResponse;
import com.quocanh.doan.dto.response.Job.JobPaginationResponse;

public interface IApplicant {
    void addApplicatnt(ApplicantRequest applicantRequest, Long idUser);
    boolean isCheckApplied(Long userId, Long jobId);
    ApplicantPaginationResponse getAllApplicant(Long id, Integer pageNo, Integer pageSize);
    ApplicantResponse getDetailsApplicantUser(Long idApply, String nameuser);

}
