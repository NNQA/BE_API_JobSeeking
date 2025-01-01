package com.quocanh.doan.Service.ImplementService.ApplicantService;

import com.quocanh.doan.Exception.Applicant.ApplicantException;
import com.quocanh.doan.Exception.Company.CompanyExeptionHanlde;
import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Mapper.ApplicantMapper;
import com.quocanh.doan.Mapper.JobMapper;
import com.quocanh.doan.Model.*;
import com.quocanh.doan.Repository.ApplicantRepository;
import com.quocanh.doan.Repository.CompanyRepository;
import com.quocanh.doan.Repository.JobRepository;
import com.quocanh.doan.Repository.UserRepository;
import com.quocanh.doan.Service.Interface.Applicant.IApplicant;
import com.quocanh.doan.dto.request.ApplicantRequest.ApplicantRequest;
import com.quocanh.doan.dto.response.ApplicantResponse.ApplicantPaginationResponse;
import com.quocanh.doan.dto.response.ApplicantResponse.ApplicantResponse;
import com.quocanh.doan.dto.response.Job.JobPaginationResponse;
import com.quocanh.doan.dto.response.Job.JobResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ApplicantService implements IApplicant {

    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    public ApplicantService(ApplicantRepository applicantRepository, UserRepository userRepository,
                                JobRepository jobRepository, CompanyRepository companyRepository) {
        this.applicantRepository = applicantRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
    }
    @Override
    public void addApplicatnt(ApplicantRequest applicantRequest, Long idUser) {
        logger.info("############## service is working");
        try {
            logger.info("---------------- Update role user");
            User user = userRepository.findById(idUser).orElseThrow(
                    () -> new UserNotFoundException("User not found with " + idUser)
            );

            Job job = jobRepository.findById(Long.parseLong(applicantRequest.getJobId()))
                    .orElseThrow(
                            () -> new JobExcetionHandler("User not found with " + applicantRequest.getJobId())
                    );
            Applicant existingApplicant = user.getApplicants().stream()
                    .filter(applicant -> applicant.getJob().getId().equals(job.getId()))
                    .findFirst()
                    .orElse(null);
            if (existingApplicant != null) {
                logger.info("Re-applying for userId: " + idUser + " and jobId: " + job.getId());
                existingApplicant.setResumeUrl(applicantRequest.getResumeUrl());
                existingApplicant.setUpdatedDateTime(LocalDateTime.now());
            } else {
                logger.info("Creating new application for userId: " + idUser + " and jobId: " + job.getId());
                existingApplicant = new Applicant();
                existingApplicant.setUser(user);
                existingApplicant.setJob(job);
                existingApplicant.setResumeUrl(applicantRequest.getResumeUrl());

                user.getApplicants().add(existingApplicant);
                job.getApplicants().add(existingApplicant);
            }


            this.applicantRepository.save(existingApplicant);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApplicantException(e.getMessage());
        }
    }

    @Override
    public boolean isCheckApplied(Long userId, Long jobId) {
        return applicantRepository.existsApplicantByUser_IdAndJob_Id(userId, jobId);
    }

    @Override
    public ApplicantPaginationResponse getAllApplicant(Long id, Integer pageNo, Integer pageSize) {
        logger.info("############## getAllJobPage's service is working");
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Cannot find user"));
        logger.info("############## checking exist company");
        Company company = this.companyRepository.findByUser(user)
                .orElseThrow(() -> new CompanyExeptionHanlde("Cannot find company"));
        List<Job> jobList = this.jobRepository.getAllByCompany(company);
        System.out.println("job");
        System.out.println(jobList);
        logger.info("############## create pageable");
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        logger.info("############## ");
        Page<Applicant> applicants;
        try {
            applicants = applicantRepository.findApplicantsByJobs(jobList, pageable);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid page number or page size: " + ex.getMessage(), ex);
            throw new JobExcetionHandler(ex);
        } catch (DataAccessException ex) {
            logger.error("Error accessing the database: " + ex.getMessage(), ex);
            throw new JobExcetionHandler(ex);
        } catch (EntityNotFoundException ex) {
            logger.error("Applicant not found: " + ex.getMessage(), ex);
            throw new JobExcetionHandler(ex);
        }  catch (NullPointerException ex) {
            logger.error("Applicant is null: " + ex.getMessage(), ex);
            throw new JobExcetionHandler(ex);
        }
        logger.info("############## map list applicantResponses");
        System.out.println("asdsd");
        System.out.println(applicants.getContent());
        List<ApplicantResponse> applicantResponses = ApplicantMapper.INSTANCE.applicantListToAppicantResponseList(applicants.getContent());
        return new ApplicantPaginationResponse(
                applicantResponses,
                applicants.getNumber(),
                applicants.getSize(),
                applicants.getTotalElements(),
                applicants.getTotalPages(),
                applicants.isLast()
        );
    }

    @Override
    public ApplicantResponse getDetailsApplicantUser(Long idApply, String username) {
        System.out.println(
                idApply
        );
        System.out.println(
                username
        );
        Applicant applicant = applicantRepository.findByIdAndAndUser_Name(idApply, username)
                .orElseThrow(
                        () -> new ApplicantException("Cannot find applicant")
                );
        return ApplicantMapper.INSTANCE.applicantToResponse(applicant);
    }
}
