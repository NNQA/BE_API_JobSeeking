package com.quocanh.doan.Service.ImplementService.ClientImplementService;

import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Mapper.JobMapper;
import com.quocanh.doan.Model.Address;
import com.quocanh.doan.Model.Job;
import com.quocanh.doan.Model.JobCategory;
import com.quocanh.doan.Model.Salary;
import com.quocanh.doan.Repository.AddressRepository;
import com.quocanh.doan.Repository.JobCategoryRepository;
import com.quocanh.doan.Repository.JobRepository;
import com.quocanh.doan.Repository.SalaryRepository;
import com.quocanh.doan.Service.Interface.ClientRequest.IClientRequest;
import com.quocanh.doan.dto.response.Job.JobResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
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

    private final SalaryRepository salaryRepository;

    public ClientImplementService(JobRepository jobRepository, AddressRepository addressRepository,
                                  JobCategoryRepository jobCategoryRepository, SalaryRepository salaryRepository) {
        this.jobRepository = jobRepository;
        this.addressRepository = addressRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.salaryRepository = salaryRepository;
    }
    @Override
    public List<JobResponse> searchJobRequestFromClient(String title, String provinceName, String cate, String experience,
                                                        String jobType, String level, Integer salary) {
        SearchSession searchSession = Search.session( entityManager );
        MassIndexer indexer = searchSession.massIndexer( Job.class )
                .idFetchSize( 150 )
                .batchSizeToLoadObjects( 25 )
                .threadsToLoadObjects( 12 );
        try {
            indexer.startAndWait();
            SearchResult<Job> result = searchSession.search(Job.class)
                    .where(f -> {
                        BooleanPredicateClausesStep<?> bool = f.bool();
                        addMatchCondition(bool, f, "title", title);
                        addMatchCondition(bool, f, "address.provinceName", provinceName);
                        addMatchCondition(bool, f, "categories.jobCategoryName", cate);
                        addMatchCondition(bool, f, "experience", experience);
                        addMatchCondition(bool, f, "type.jobTypeName", jobType);
                        addMatchCondition(bool, f, "position.jobPositionName", level);

                        if (salary != null) {
                            bool.must(f.range().field("salary.numberSort").atMost(salary));
                        }

                        return bool;
                    }).fetch(20);
            System.out.println(result.hits());
            return JobMapper.INSTANCE.jobListToJobResponseList(result.hits());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
    public JobResponse getJobDetailsWithJob(String title) {
        Optional<Job> job = Optional.ofNullable(jobRepository.findByTitleContaining(title).orElseThrow(
                () -> new JobExcetionHandler("Cannot find the job")
        ));
        return JobMapper.INSTANCE.jobToJobResponse(job.get());
    }

    private void addMatchCondition(BooleanPredicateClausesStep<?> bool, SearchPredicateFactory f, String field, String value) {
        if (value != null && !value.isEmpty()) {
            bool.must(f.match().fields(field).matching(value));
        }
    }

    private void addSalaryRangeCondition(BooleanPredicateClausesStep<?> bool, SearchPredicateFactory f, String salary) {
        if(salary != null && !salary.isEmpty()) {
            String salaryStr = salary.replace(" triệu","").trim();
            Integer lowerBound = null;
            Integer upperBound = null;
            try {
                if(salaryStr.startsWith("Dưới")) {
//                    upperBound = Integer.parseInt()
                }

                bool.must(
                        f.match().field("salary").matching(salary)
                );
            } catch (NumberFormatException e) {
                throw new JobExcetionHandler("Format in salary is wrong");
            }
        }
    }
}
