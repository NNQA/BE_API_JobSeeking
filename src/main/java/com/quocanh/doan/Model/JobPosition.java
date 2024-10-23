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
@Table(name = "job_position")
public class JobPosition {
    private static final Integer STATUS_INACTIVE = 0;
    private static final Integer STATUS_ACTIVE = 1;

    @JsonIgnore
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Job> listJob = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="job_position_name")
    @NotNull(message = "Job type name must be provided")
    private String jobPositionName;

    @Max(1)
    @Min(0)
    @Column(name = "status", nullable = false)
    private Integer status;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    public JobPosition(String jobPositionName) {
        this.jobPositionName = jobPositionName;
        this.status = JobPosition.getStatusActive();
    }

    public JobPosition() {

    }

    public static Integer getStatusInactive() {
        return JobPosition.STATUS_INACTIVE;
    }
    public static Integer getStatusActive() {
        return  JobPosition.STATUS_ACTIVE;
    }
}
