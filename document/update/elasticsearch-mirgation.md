# Elasticsearch Migration Notes

### **Migration from Lucene to Elasticsearch**

### **1. Existing Code Snippet**

```java
@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
@JoinColumn(name = "address")
@NotNull(message = "Address must be provided.")
@JsonIgnore
@ToString.Exclude
@IndexedEmbedded(includePaths = {"provinceName"})
@IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
private Address address;
```

### **2. Purpose of This Code**

- Defines a many-to-one relationship with the `Address` entity.
- Refreshes data when referenced entities are updated.
- Loads `Address` lazily (only when accessed).
- Specifies the foreign key column in the database.
- Ensures `Address` cannot be null.
- Prevents serialization in JSON responses.
- Avoids infinite loops when using Lombok's `toString()`.
- Used in Hibernate Search (Lucene) to embed `provinceName` for indexing.
- Prevents reindexing when `Address` updates.

### **3. Migration Considerations**

Since Elasticsearch does not work with Hibernate Search directly, some adaptations are needed:

- **No `@IndexedEmbedded` equivalent** → Use manual indexing in an Elasticsearch service.
- **Reindexing Strategy** → Manually update Elasticsearch when `Address` changes.

### **4. Future Reference**

- If reusing Hibernate Search with Elasticsearch (`hibernate-search-elasticsearch`), update the indexing logic accordingly.
- Store related data in a separate index and use `nested` or `join` queries in Elasticsearch.

### **5. Additional Notes**

- Keep this `.md` file updated with any further Elasticsearch-specific adaptations.
- **Configuration in `application.yml`:**

```yaml
# properties:
#   hibernate:
##     search:
##       automatic_indexing:
##         enabled: true
##       backend:
##         lucene_version: LUCENE_8_1_1
##         directory:
##           type: local-filesystem
##           root: ./lucene-data
##         analysis:
##           configurer: com.quocanh.doan.config.ConfigLucene.CustomAnalyzerConfigurer
##         schema_management:
##           strategy: drop-and-create
##         indexing:
##           thread_pool:
##             size: 4
##           queue_size: 1000
##           queue_count: 10
##           batch_size: 50
#     jdbc:
#       batch_size: 10
#   open-in-view: true
```

