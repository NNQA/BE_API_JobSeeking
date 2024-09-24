package com.quocanh.doan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
@Table(name = "job_category")
public class JobCategory {
    private static final Integer STATUS_INACTIVE = 0;
    private static final Integer STATUS_ACTIVE = 1;
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Job> listJob = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @Column(name="job_category_name")
    @Pattern(regexp = "^[A-Za-z-, \\.\\/\\(\\)]{2,}$", message="Job category name is not valid.")
    @NotNull(message = "Job category name must be provided")
    private String jobCategoryName;

    @Max(1)
    @Min(0)
    @Column(name = "status", nullable = false)
    private Integer status;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    public JobCategory(String jobCategoryNameName) {
        this.jobCategoryName = jobCategoryNameName;
        this.status = JobPosition.getStatusActive();
    }

    public JobCategory() {

    }

    public static Integer getStatusInactive() {
        return JobCategory.STATUS_INACTIVE;
    }
    public static Integer getStatusActive() {
        return  JobCategory.STATUS_ACTIVE;
    }
}
