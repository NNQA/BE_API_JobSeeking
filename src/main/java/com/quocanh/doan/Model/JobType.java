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
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
@Table(name = "job_types")
public class JobType {
    private static final Integer STATUS_INACTIVE = 0;
    private static final Integer STATUS_ACTIVE = 1;

    @JsonIgnore
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Job> listJob = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="job_type_name")
    @Pattern(regexp = "^[A-Za-z-, \\.\\/\\(\\)]{2,}$", message="Job type name is not valid.")
    @NotNull(message = "Job type name must be provided")
    @FullTextField
    private String jobTypeName;

    @Max(1)
    @Min(0)
    @Column(name = "status", nullable = false)
    private Integer status;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    public JobType(String jobTypeName) {
        this.jobTypeName = jobTypeName;
        this.status = JobType.getStatusActive();
    }

    public JobType() {
    }

    public static Integer getStatusInactive() {
        return JobType.STATUS_INACTIVE;
    }

    public static Integer getStatusActive() {
        return JobType.STATUS_ACTIVE;
    }
}
