package com.quocanh.doan.Security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;

@Component
public class BuildSearchIndex {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void buildIndex() {
        SearchSession searchSession = Search.session(entityManager);
        try {
            searchSession.massIndexer().startAndWait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
