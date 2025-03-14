package com.quocanh.doan.Service.ImplementService.ClientImplementService;

import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Mapper.JobMapper;
import com.quocanh.doan.Model.*;
import com.quocanh.doan.Repository.*;
import com.quocanh.doan.Service.Interface.ClientRequest.IClientRequest;
import com.quocanh.doan.dto.response.Job.JobResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientImplementService implements IClientRequest {

    private final JobRepository jobRepository;
    private final AddressRepository addressRepository;

    private final JobCategoryRepository jobCategoryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final JobPostionRepository jobPostionRepository;
//    private final SalaryRepository salaryRepository;

    public ClientImplementService(JobRepository jobRepository, AddressRepository addressRepository,
                                  JobCategoryRepository jobCategoryRepository,
                                  JobPostionRepository jobPostionRepository) {
        this.jobRepository = jobRepository;
        this.addressRepository = addressRepository;
        this.jobCategoryRepository = jobCategoryRepository;
//        this.salaryRepository = salaryRepository;
        this.jobPostionRepository = jobPostionRepository;
    }
    @Override
    @Transactional
    public List<JobResponse> searchJobRequestFromClient(String title, String provinceName, String cate, String experience,
                                                        String jobType, String level, Integer salary) {

        SearchSession searchSession = Search.session(entityManager);

        try {
            SearchResult<Job> result = searchSession.search(Job.class)
                    .where(f ->f.bool()
                            .must(f.match().field("status").matching(Job.getStatusActive()))
                            .must(buildSearchPredicate(f, title, provinceName, cate,
                                    experience, jobType, level, salary)))
//                     .sort(f -> f.field("createdDateTime").desc())
                    .fetch(20);
            return result.hits().stream()
                    .map(JobMapper.INSTANCE::jobToJobResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void addMatchPredicateIfPresent(BooleanPredicateClausesStep<?> bool,
                                            SearchPredicateFactory f, String field, String value, float boost) {
        if (StringUtils.isNotBlank(value)) {
            bool.must(f.match()
                    .field(field)
                    .boost(boost)
                    .matching(value));
        }
    }


    private BooleanPredicateClausesStep<?> buildSearchPredicate(SearchPredicateFactory f, String title,
                                                 String provinceName, String category, String experience, String jobType,
                                                 String level, Integer salary) {

        BooleanPredicateClausesStep<?> bool = f.bool();

        Optional.ofNullable(title)
                .ifPresent(t -> addMatchPredicateIfPresent(bool, f, "title", t, 2.0f));
        Optional.ofNullable(provinceName)
                .ifPresent(t ->  addMatchPredicateIfPresent(bool, f, "address.provinceName", provinceName, 1.0f));
        Optional.ofNullable(category)
                .ifPresent(t -> addMatchPredicateIfPresent(bool, f, "categories.jobCategoryName", category, 1.5f));
        Optional.ofNullable(experience)
                .ifPresent(t ->  addMatchPredicateIfPresent(bool, f, "experience", experience, 1.0f));
        Optional.ofNullable(jobType)
                .ifPresent(t -> addMatchPredicateIfPresent(bool, f, "type.jobTypeName", jobType, 1.0f));
        Optional.ofNullable(level)
                .ifPresent(t -> addMatchPredicateIfPresent(bool, f, "position.jobPositionName", level, 1.0f));


        if (salary != null) {
            int lowerBound = (int)(salary * 0.9); // 10% below target
            int upperBound = (int)(salary * 1.1); // 10% above target

            bool.must(f.range()
                    .field("salary.numberSort")
                    .between(lowerBound, upperBound));
        }

//        bool.must(f.match()
//                .field("status")
//                .matching("ACTIVE"));

        return bool;
    }

    @Override
    public Set<String> getAllProvinceName() {
        return addressRepository.findAll().stream()
                .map(Address::getProvinceName).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAllCategoryName() {
        return jobCategoryRepository.findAll().stream()
                .map(JobCategory::getJobCategoryName).collect(Collectors.toSet());
    }

    @Override
    public List<String> getPositionName() {
        return jobPostionRepository.findAll().stream()
                .map(JobPosition::getJobPositionName)
                .peek(name -> System.out.println("Mapping job position name: " + name))
                .collect(Collectors.toList());
    }


    @Override
    public JobResponse getJobDetailsWithJob(String title, Long id) {
        Optional<Job> job = Optional.ofNullable(jobRepository.findById(id).orElseThrow(
                () -> new JobExcetionHandler("Cannot find the job")
        ));
        return JobMapper.INSTANCE.jobToJobResponse(job.get());
    }

    @Override
    public List<JobResponse> getNewJob() {
        List<Job> jobList = jobRepository.findTop9ByOrderByCreatedDateTimeDesc();
        return JobMapper.INSTANCE.jobListToJobResponseList(jobList);
    }

}
