package com.quocanh.doan.Service.ImplementService.IndexService;

import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Model.Job;
import com.quocanh.doan.Service.Interface.IIndex.IIndex;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class IndexService implements IIndex {
    @PersistenceContext
    private final EntityManager entityManager;
    @Value("${indexer.batch.size:50}")
    private int batchSize;
    private SearchSession searchSession;

    @Value("${indexer.threads:4}")
    private int threads;
    private final Logger logger = (Logger) LoggerFactory.getLogger(IndexService.class);
    public IndexService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Transactional
    public void rebuildIndex() {
        logger.info("############## reindex service is working");
        searchSession = Search.session(entityManager);

        try {
            MassIndexer indexer = searchSession.massIndexer()
                    .dropAndCreateSchemaOnStart(true)
                    .threadsToLoadObjects(threads)
                    .batchSizeToLoadObjects(batchSize)
                    .idFetchSize(150)
                    .transactionTimeout(3600)
                    .typesToIndexInParallel(2)
                    .purgeAllOnStart(true);
            indexer.startAndWait();
            logger.info("Mass indexing completed successfully");
        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();
            logger.error("Mass indexing interrupted", ex);
            throw new JobExcetionHandler(ex.getMessage());
        }
    }

    @Transactional
    public <T> void rebuildSpecificIndex(Class<T> entityClass, boolean purgeFirst) {
        try {
            logger.info("Starting mass indexing for entity: {}", entityClass.getSimpleName());
            searchSession = Search.session(entityManager);
            MassIndexer indexer = searchSession.massIndexer(entityClass)
                    .threadsToLoadObjects(threads)
                    .batchSizeToLoadObjects(batchSize)
                    .idFetchSize(150)
                    .transactionTimeout(3600)
                    .purgeAllOnStart(purgeFirst);

            indexer.startAndWait();
            logger.info("Mass indexing completed for entity: {}", entityClass.getSimpleName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Mass indexing interrupted for entity: {}", entityClass.getSimpleName(), e);
            throw new RuntimeException("Error during mass indexing", e);
        }
    }
    @Transactional
    public <T> void updateIndex(T entity) {
        try {
            logger.error("Update index is working");
            searchSession = Search.session(entityManager);
            SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
            indexingPlan.addOrUpdate(entity);

            indexingPlan.execute();
        } catch (Exception e) {
            logger.error("Failed to update index", e);
            throw new RuntimeException("Index update failed", e);
        }
    }

//    // Batch processing with progress monitoring
//    @Transactional
//    public <T> void batchUpdateIndex(List<T> entities) {
//        if (entities.isEmpty()) {
//            return;
//        }
//
//        try {
//            SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
//            AtomicInteger counter = new AtomicInteger(0);
//
//            entities.forEach(entity -> {
//                indexingPlan.addOrUpdate(entity);
//                int processed = counter.incrementAndGet();
//                if (processed % batchSize == 0) {
//                    log.info("Processed {} of {} entities", processed, entities.size());
//                }
//            });
//
//            indexingPlan.execute();
//            log.info("Batch indexing completed for {} entities", entities.size());
//        } catch (Exception e) {
//            log.error("Batch index update failed", e);
//            throw new RuntimeException("Batch index update failed", e);
//        }
//    }
}
