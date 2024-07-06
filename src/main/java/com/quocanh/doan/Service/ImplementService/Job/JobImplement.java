package com.quocanh.doan.Service.ImplementService.Job;

import com.quocanh.doan.Exception.Company.CompanyExeptionHanlde;
import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Model.*;
import com.quocanh.doan.Repository.*;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.Interface.Job.IJob;
import com.quocanh.doan.dto.request.Job.JobRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class JobImplement implements IJob {

    private final JobRepository jobRepository;
    private final JobTypeRepository jobTypeRepository;
    private final JobPostionRepository jobPostionRepository;
    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    public JobImplement(JobRepository jobRepository, CompanyRepository companyRepository,
                        JobTypeRepository jobTypeRepository, JobPostionRepository jobPostionRepository,
                        UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.jobTypeRepository = jobTypeRepository;
        this.jobPostionRepository = jobPostionRepository;
        this.companyRepository =companyRepository;
        this.userRepository = userRepository;
    }
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private String[] createMessageError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).toArray(String[]::new);
    }
    @Override
    @Transactional
    public void addNewJob(JobRequest request, UserPrincipal userPrincipal, BindingResult result) {
        logger.info("############## service is working");

        if (result.hasErrors()) {
            logger.info("------------ validation error" + String.join(" ", createMessageError(result)));
            throw new JobExcetionHandler(createMessageError(result)[0]);
        }
        try {
            logger.info("############## checking exist user");

            User user = this.userRepository.findById(userPrincipal.getId())
                            .orElseThrow(() -> new UserNotFoundException("Cannot find user"));
            logger.info("############## checking exist company");
            Company company = this.companyRepository.findByUser(user)
                    .orElseThrow(() -> new CompanyExeptionHanlde("Cannot find company"));
            logger.info("############## checking exist job type");
            JobType jobType = this.jobTypeRepository.findByJobTypeName(request.getJobTypeRequest().getJobTypeName())
                    .orElseGet(
                            () -> {
                                JobType jobType1 = new JobType(request.getJobTypeRequest().getJobTypeName());
                                return this.jobTypeRepository.save(jobType1);
                            }

                    );
            logger.info("############## checking exist job position");
            JobPosition jobPosition = this.jobPostionRepository.findByJobPositionName(request.getPosition().getJobPositionName())
                    .orElseGet(
                            () ->  {
                                 JobPosition jobPosition1 = new JobPosition(request.getPosition().getJobPositionName());
                                 return this.jobPostionRepository.save(jobPosition1);
                            }
                    );

            logger.info("############## checking expired job in day");
            Optional<Job> existingOpt = this.jobRepository.findByTitleAndCompanyAndTypeAndPosition(
                    request.getTitle(), company, jobType, jobPosition
            );

            if(existingOpt.isPresent()) {
                Job existing = existingOpt.get();

                if(existing.getExpiredDate().isAfter(LocalDateTime.now())) {
                    throw new JobExcetionHandler("A job with the same content already exist and is not expired");
                }
            }


            logger.info("############## create new job");
            Job newJob = new Job();
            newJob.setActiveDate(request.getActiveDate());
            newJob.setExpiredDate(request.getExpiredDate());
            newJob.setCompany(company);
            newJob.setDescription(request.getDescription());
            newJob.setSalary(request.getSalary());
            newJob.setTitle(request.getTitle());
            newJob.setType(jobType);
            newJob.setPosition(jobPosition);



            logger.info("############## store job to database");

            this.jobRepository.save(newJob);

            logger.info("############## ok");

        } catch (RuntimeException e) {
            logger.info("-------------- having trouble service with " + e.getMessage());
            throw new JobExcetionHandler(e.getMessage());
        }
    }
}
