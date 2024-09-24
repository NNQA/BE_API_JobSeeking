package com.quocanh.doan.Service.ImplementService.Job;

import com.quocanh.doan.Exception.Company.CompanyExeptionHanlde;
import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Model.*;
import com.quocanh.doan.Repository.*;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.Interface.Job.IJob;
import com.quocanh.doan.dto.request.Job.JobCategoryRequest;
import com.quocanh.doan.dto.request.Job.JobRequest;
import com.quocanh.doan.dto.request.Job.SkillRequest;
import com.quocanh.doan.dto.response.Job.JobTypeResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

    private  final AddressRepository addressRepository;

    public JobImplement(JobRepository jobRepository, CompanyRepository companyRepository,
                        JobTypeRepository jobTypeRepository, JobPostionRepository jobPostionRepository,
                        UserRepository userRepository, SkillRepository skillRepository,
                        JobCategoryRepository jobCategoryRepository, AddressRepository addressRepository) {
        this.jobRepository = jobRepository;
        this.jobTypeRepository = jobTypeRepository;
        this.jobPostionRepository = jobPostionRepository;
        this.companyRepository =companyRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.addressRepository = addressRepository;
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
            JobType jobType = this.jobTypeRepository.findByJobTypeName(request.getJobType().getJobTypeName())
                    .orElseGet(
                            () -> {
                                JobType jobType1 = new JobType(request.getJobType().getJobTypeName());
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

            logger.info("############## create list skills");
            Set<Skill> skills = manageSkill(request.getSkills());

            logger.info("############## create category");
            Set<JobCategory> categories = manageCategory(request.getJobCategories());

            logger.info("############## checking exist address");
            System.out.println(request.getAddress());
            Address address  = this.addressRepository.findByProvinceNameAndDistrictNameAndCommuneNameAndLngAndLat(
                    request.getAddress().getProvinceName(),
                            request.getAddress().getDistrictName(), request.getAddress().getCommuneName()
                    , request.getAddress().getLng(),
                            request.getAddress().getLat()
                    )
                    .orElseGet(
                            () ->  {
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
            if(job == null) {
                throw new JobExcetionHandler("Unable to find job with id" + Long.toString(id));
            }
            System.out.println("Company ");
            System.out.println(job.getCompany());;
            logger.info("############## ok");
            return job;
        }
        catch (Exception e) {
            logger.info("Error occurred during 'Checking database'.");
            throw new JobExcetionHandler("Error occurred during 'Checking database'.");
        }
    }

    private Set<Skill> manageSkill(@NotNull(message = "Skill requests must be provided") @Size(min = 1, max = 10, message = "There must be between 1 and 10 skills") Set<SkillRequest> requestSkill) {
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
        return skills;
    }

    private Set<JobCategory> manageCategory(@NotNull(message = "Category requests must be provided") @Size(min = 1, max = 10, message = "There must be between 1 and 10 skills") Set<JobCategoryRequest> requestCategory) {
        Set<String> nameJobCategoryRequest = requestCategory.stream().map(JobCategoryRequest::getJobNameCategory)
                .collect(Collectors.toSet());
        System.out.println(requestCategory);
        System.out.println(nameJobCategoryRequest);
        List<JobCategory> existingJobCategory = this.jobCategoryRepository.findByJobCategoryName(nameJobCategoryRequest);
        Map<String, JobCategory> mapJobCategory = existingJobCategory.stream().collect(
                Collectors.toMap(JobCategory::getJobCategoryName, jobCategory -> jobCategory)
        );


        Set<JobCategory> newJobCategories = requestCategory.stream()
                .map(JobCategoryRequest::getJobNameCategory)
                .filter(name -> !mapJobCategory.containsKey(name))
                .map(JobCategory::new)
                .collect(Collectors.toSet());
        if (!newJobCategories.isEmpty()) {
            this.jobCategoryRepository.saveAll(newJobCategories);
        }
        Set<JobCategory> jobCategories = requestCategory.stream()
                .map(JobCategoryRequest::getJobNameCategory)
                .map(mapJobCategory::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        jobCategories.addAll(newJobCategories);
        return jobCategories;
    }



}
