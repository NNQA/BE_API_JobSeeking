package com.quocanh.doan.Service.ImplementService.ClientImplementService;

import com.quocanh.doan.Mapper.JobMapper;
import com.quocanh.doan.Model.Job;
import com.quocanh.doan.Repository.JobRepository;
import com.quocanh.doan.Service.Interface.ClientRequest.IClientRequest;
import com.quocanh.doan.dto.response.Job.JobResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.lucene.search.TopDocs;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchQuery;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchResult;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientImplementService implements IClientRequest {

    private final JobRepository jobRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ClientImplementService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    @Override
    public List<JobResponse> searchJobRequestFromClient(String title) {
        SearchSession searchSession = Search.session( entityManager );
        MassIndexer indexer = searchSession.massIndexer( Job.class )
                .idFetchSize( 150 )
                .batchSizeToLoadObjects( 25 )
                .threadsToLoadObjects( 12 );
        try {
            indexer.startAndWait();
            SearchResult<Job> result = searchSession.search(Job.class)
                    .where(f -> f.bool()
                            .must(f.match()
                                    .fields("title")
                                    .matching(title))
                            .must(f.match()
                                    .fields("address.provinceName")
                                    .matching("Hồ Chí Minh")))
                    .fetch(20);

            return JobMapper.INSTANCE.jobListToJobResponseList(result.hits());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
