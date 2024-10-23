package com.quocanh.doan.Service.ImplementService.Job;

import ch.qos.logback.core.joran.spi.JoranException;
import com.quocanh.doan.Exception.Company.CompanyExeptionHanlde;
import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Mapper.JobMapper;
import com.quocanh.doan.Model.*;
import com.quocanh.doan.Repository.*;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.Interface.Job.IJob;
import com.quocanh.doan.dto.request.Job.JobCategoryRequest;
import com.quocanh.doan.dto.request.Job.JobRequest;
import com.quocanh.doan.dto.request.Job.SkillRequest;
import com.quocanh.doan.dto.response.Job.JobPaginationResponse;
import com.quocanh.doan.dto.response.Job.JobResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobImplement implements IJob {

    private final JobRepository jobRepository;
    private final JobTypeRepository jobTypeRepository;
    private final JobPostionRepository jobPostionRepository;
    private final CompanyRepository companyRepository;

    private final JobCategoryRepository jobCategoryRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    private final AddressRepository addressRepository;

    public JobImplement(JobRepository jobRepository, CompanyRepository companyRepository,
                        JobTypeRepository jobTypeRepository, JobPostionRepository jobPostionRepository,
                        UserRepository userRepository, SkillRepository skillRepository,
                        JobCategoryRepository jobCategoryRepository, AddressRepository addressRepository) {
        this.jobRepository = jobRepository;
        this.jobTypeRepository = jobTypeRepository;
        this.jobPostionRepository = jobPostionRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.addressRepository = addressRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
            JobType jobType = this.jobTypeRepository.findByJobTypeName(request.getType().getJobTypeName())
                    .orElseGet(
                            () -> {
                                JobType jobType1 = new JobType(request.getType().getJobTypeName());
                                return this.jobTypeRepository.save(jobType1);
                            }

                    );
            logger.info("############## checking exist job position");
            JobPosition jobPosition = this.jobPostionRepository.findByJobPositionName(request.getPosition().getJobPositionName())
                    .orElseGet(
                            () -> {
                                JobPosition jobPosition1 = new JobPosition(request.getPosition().getJobPositionName());
                                return this.jobPostionRepository.save(jobPosition1);
                            }
                    );

            logger.info("############## checking expired job in day");
            Optional<Job> existingOpt = this.jobRepository.findByTitleAndCompanyAndTypeAndPosition(
                    request.getTitle(), company, jobType, jobPosition
            );

            if (existingOpt.isPresent()) {
                Job existing = existingOpt.get();

                if (existing.getExpiredDate().isAfter(LocalDateTime.now())) {
                    throw new JobExcetionHandler("A job with the same content already exist and is not expired");
                }
            }



            logger.info("############## checking exist address");
            System.out.println(request.getAddress());
            Address address = this.addressRepository.findByProvinceNameAndDistrictNameAndCommuneNameAndLngAndLat(
                            request.getAddress().getProvinceName(),
                            request.getAddress().getDistrictName(), request.getAddress().getCommuneName()
                            , request.getAddress().getLng(),
                            request.getAddress().getLat()
                    )
                    .orElseGet(
                            () -> {
                                Address address1 = new Address(
                                        request.getAddress().getProvinceName(),
                                        request.getAddress().getDistrictName(), request.getAddress().getCommuneName(),
                                        request.getAddress().getFormattedAddressName()
                                        , request.getAddress().getLng(),
                                        request.getAddress().getLat()
                                );
                                return this.addressRepository.save(address1);
                            }
                    );


            logger.info("############## create new job");
            Job newJob = new Job();

            logger.info("############## create list skills");
            Set<Skill> skills = manageSkill(request.getSkills(),newJob);

            logger.info("############## create category");
            Set<JobCategory> categories = manageCategory(request.getCategories(), newJob);

            newJob.setExpiredDate(request.getExpiredDate());
            newJob.setCompany(company);
            newJob.setSalary(request.getSalary());
            newJob.setSkills(skills);
            newJob.setCategories(categories);
            newJob.setDescription(request.getDescription());
            newJob.setSalary(request.getSalary());
            newJob.setTitle(request.getTitle());
            newJob.setType(jobType);
            newJob.setPosition(jobPosition);
            newJob.setExperience(request.getExperience());
            newJob.setAddress(address);

            logger.info("############## store job to database");

            this.jobRepository.save(newJob);

            logger.info("############## ok");

        } catch (RuntimeException e) {
            logger.info("-------------- having trouble service with " + e.getMessage());
            throw new JobExcetionHandler(e.getMessage());
        }
    }

    @Override
    public void updateJob(Long id,JobRequest request, UserPrincipal userPrincipal, BindingResult result) {
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

            Optional<Job> existingOpt = this.jobRepository.findByIdAndCompanyWithDetailsWithOptional(id, company.getId());
            if(existingOpt.isEmpty()) {
                throw new JobExcetionHandler("Job is not exist!!!!");
            }

            logger.info("############## checking exist job type");
            JobType jobType = this.jobTypeRepository.findByJobTypeName(request.getType().getJobTypeName())
                    .orElseGet(
                            () -> {
                                JobType jobType1 = new JobType(request.getType().getJobTypeName());
                                return this.jobTypeRepository.save(jobType1);
                            }

                    );
            logger.info("############## checking exist job position");
            JobPosition jobPosition = this.jobPostionRepository.findByJobPositionName(request.getPosition().getJobPositionName())
                    .orElseGet(
                            () -> {
                                JobPosition jobPosition1 = new JobPosition(request.getPosition().getJobPositionName());
                                return this.jobPostionRepository.save(jobPosition1);
                            }
                    );

            logger.info("############## checking exist address");
            System.out.println(request.getAddress());

            Address address = this.addressRepository.findByProvinceNameAndDistrictNameAndCommuneNameAndLngAndLat(
                            request.getAddress().getProvinceName(),
                            request.getAddress().getDistrictName(), request.getAddress().getCommuneName()
                            , request.getAddress().getLng(),
                            request.getAddress().getLat()
                    )
                    .orElseGet(
                            () -> {
                                Address address1 = new Address(
                                        request.getAddress().getProvinceName(),
                                        request.getAddress().getDistrictName(), request.getAddress().getCommuneName(),
                                        request.getAddress().getFormattedAddressName()
                                        , request.getAddress().getLng(),
                                        request.getAddress().getLat()
                                );
                                return this.addressRepository.save(address1);
                            }
                    );
            logger.info("############## create list skills");
            Set<Skill> skills = manageSkill(request.getSkills(), existingOpt.get());



            logger.info("############## create category");
            Set<JobCategory> categories = manageCategory(request.getCategories(), existingOpt.get());

            logger.info("############## create new job");

            existingOpt.get().setExpiredDate(request.getExpiredDate());
            existingOpt.get().setCompany(company);
            existingOpt.get().setSalary(request.getSalary());
            existingOpt.get().setSkills(skills);
            existingOpt.get().setCategories(categories);
            existingOpt.get().setDescription(request.getDescription());
            existingOpt.get().setSalary(request.getSalary());
            existingOpt.get().setTitle(request.getTitle());
            existingOpt.get().setType(jobType);
            existingOpt.get().setPosition(jobPosition);
            existingOpt.get().setExperience(request.getExperience());
            existingOpt.get().setAddress(address);
            this.jobRepository.save(existingOpt.get());

        }
        catch (RuntimeException e) {
            logger.info("-------------- having trouble service with " + e.getMessage());
            throw new JobExcetionHandler(e.getMessage());
        }

    }

    @Override
    public List<Job> getAllJob() {
        logger.info("############## getJobService is working");
        try {
            return this.jobRepository.findAll();
        } catch (RuntimeException e) {
            logger.info("-------------- having trouble service with " + e.getMessage());
            throw new JobExcetionHandler(e.getMessage());
        }
    }

    @Override
    public Job getById(Long id) {
        try {
            logger.info("############## getById is working");
            Job job = jobRepository.getById(id);
            if (job == null) {
                throw new JobExcetionHandler("Unable to find job with id" + id);
            }
            System.out.println("Company ");
            System.out.println(job.getCompany());
            logger.info("############## ok");
            return job;
        } catch (Exception e) {
            logger.info("Error occurred during 'Checking database'.");
            throw new JobExcetionHandler("Error occurred during 'Checking database'.");
        }
    }
    @Override
    public JobResponse getByIdForCompany(Long id, Long idUser) {
        try {
            logger.info("############## getByIdForCompany is working");
            User user = this.userRepository.findById(idUser)
                    .orElseThrow(() -> new UserNotFoundException("Cannot find user"));
            logger.info("############## checking exist company");
            Company company = this.companyRepository.findByUser(user)
                    .orElseThrow(() -> new CompanyExeptionHanlde("Cannot find company"));
            logger.info("############## get job");
            Job job = jobRepository.findByIdAndCompanyWithDetails(id, company.getId());
            JobResponse jobResponses = JobMapper.INSTANCE.jobToJobResponse(job);
            if (jobResponses == null) {
                throw new JobExcetionHandler("Unable to find job with id" + id);
            }
            logger.info("############## ok");
            return jobResponses;
        } catch (Exception e) {
            logger.info("Error occurred during 'Checking database'.");
            throw new JobExcetionHandler("Error occurred during 'Checking database'.");
        }
    }

    @Override
    public JobPaginationResponse getAllJobPage(Long id, Integer pageNo, Integer pageSize) {
        logger.info("############## getAllJobPage's service is working");
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Cannot find user"));
        logger.info("############## checking exist company");
        Company company = this.companyRepository.findByUser(user)
                .orElseThrow(() -> new CompanyExeptionHanlde("Cannot find company"));


        logger.info("############## create pageable");
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        logger.info("############## ");
        Page<Job> jobs;
        try {
             jobs = jobRepository.findAllByCompany(company, pageable);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid page number or page size: " + ex.getMessage(), ex);
            throw new JobExcetionHandler(ex); // or handle it accordingly
        } catch (DataAccessException ex) {
            logger.error("Error accessing the database: " + ex.getMessage(), ex);
            throw new JobExcetionHandler(ex);// or handle it accordingly
        } catch (EntityNotFoundException ex) {
            logger.error("Company not found: " + ex.getMessage(), ex);
            throw new JobExcetionHandler(ex); // or handle it accordingly
        }  catch (NullPointerException ex) {
            logger.error("Company is null: " + ex.getMessage(), ex);
            throw new JobExcetionHandler(ex); // or handle it accordingly
        }
        logger.info("############## map list jobResponse");
        List<JobResponse> jobResponses = JobMapper.INSTANCE.jobListToJobResponseList(jobs.getContent());

        return new JobPaginationResponse(
                jobResponses,
                jobs.getNumber(),
                jobs.getSize(),
                jobs.getTotalElements(),
                jobs.getTotalPages(),
                jobs.isLast()
        );
    }

    @Override
    public void DeleteById(Long id) {
        logger.info("############## deleted job service is working");
        try {
            this.jobRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            logger.error(ex.getMessage());
            throw new JobExcetionHandler("Cannot delete this job!!!");
        }
    }

    private Set<Skill> manageSkill(@NotNull(message = "Skill requests must be provided") @Size(min = 1, max = 10, message = "There must be between 1 and 10 skills") Set<SkillRequest> requestSkill, Job job) {
        Set<String> nameSkillsRequest = requestSkill.stream().map(SkillRequest::getNameSkill)
                .collect(Collectors.toSet());


        List<Skill> existingSkills = this.skillRepository.findByNameSkills(nameSkillsRequest);
        Map<String, Skill> skillmap = existingSkills.stream().collect(
                Collectors.toMap(Skill::getNameSkill, skill -> skill)
        );


        Set<Skill> newSkills = requestSkill.stream()
                .map(SkillRequest::getNameSkill)
                .filter(name -> !skillmap.containsKey(name))
                .map(Skill::new)
                .collect(Collectors.toSet());
        if (!newSkills.isEmpty()) {
            this.skillRepository.saveAll(newSkills);
        }
        Set<Skill> skills = requestSkill.stream()
                .map(SkillRequest::getNameSkill)
                .map(skillmap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        skills.addAll(newSkills);
        Set<Skill> currentSkills = job.getSkills();

        Set<Skill> skillsToRemove = currentSkills.stream()
                .filter(skill -> !skills.contains(skill))
                .collect(Collectors.toSet());

        skillsToRemove.forEach(skill -> {
            job.getSkills().remove(skill);
            skill.getListJobs().remove(job);
        });
        skills.forEach(skill -> {
            skill.getListJobs().add(job);
        });
        return skills;
    }

    private Set<JobCategory> manageCategory(@NotNull(message = "Category requests must be provided") @Size(min = 1, max = 10, message = "There must be between 1 and 10 skills") Set<JobCategoryRequest> requestCategory, Job job) {
        Set<String> nameJobCategoryRequest = requestCategory.stream().map(JobCategoryRequest::getJobCategoryName)
                .collect(Collectors.toSet());

        List<JobCategory> existingJobCategory = this.jobCategoryRepository.findByJobCategoryName(nameJobCategoryRequest);
        Map<String, JobCategory> mapJobCategory = existingJobCategory.stream().collect(
                Collectors.toMap(JobCategory::getJobCategoryName, jobCategory -> jobCategory)
        );


        Set<JobCategory> newJobCategories = requestCategory.stream()
                .map(JobCategoryRequest::getJobCategoryName)
                .filter(name -> !mapJobCategory.containsKey(name))
                .map(JobCategory::new)
                .collect(Collectors.toSet());
        if (!newJobCategories.isEmpty()) {
            this.jobCategoryRepository.saveAll(newJobCategories);
        }
        Set<JobCategory> jobCategories = requestCategory.stream()
                .map(JobCategoryRequest::getJobCategoryName)
                .map(mapJobCategory::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        jobCategories.addAll(newJobCategories);
        Set<JobCategory> currentCategory = job.getCategories();

        Set<JobCategory> categoriesToRemove = currentCategory.stream()
                .filter(skill -> !jobCategories.contains(skill))
                .collect(Collectors.toSet());

        categoriesToRemove.forEach(category -> {
            job.getCategories().remove(category);
            category.getListJob().remove(job);
        });
        jobCategories.forEach(jobCategory -> {
            jobCategory.getListJob().add(job);
        });
        return jobCategories;
    }


}
