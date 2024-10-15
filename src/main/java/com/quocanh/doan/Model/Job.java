package com.quocanh.doan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "Job")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "company" })
public class Job {
    private static final Integer STATUS_INACTIVE = 0;
    private static final Integer STATUS_ACTIVE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "title")
    @NotNull(message = "Title must be provided")
    private String title;

    @JoinColumn(name = "description")
    @NotNull(message = "Description must be provided")
    private String description;

    @JoinColumn(name = "experience")
    @NotNull(message = "Experience must be provided")
    private String experience;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "address")
    @NotNull(message = "Address must be provided.")
    @JsonIgnore
    @ToString.Exclude
    private Address address;


    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "position")
    @NotNull(message = "Position must be provided")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private JobPosition position;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employer")
    @NotNull(message = "Employer must be provided")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Company company;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    @NotNull(message = "JobType must be provided")
    private JobType type;


    @ManyToMany
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Skill> skills = new HashSet<>();

    @NotNull(message = "Job Category must be provided")
    @ManyToMany
    @JoinTable(
            name = "job_categories",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<JobCategory> categories = new HashSet<>();

    @JoinColumn(name = "salary")
    @NotNull(message = "Salary must be provided")
    private String salary;

    @JoinColumn(name = "expiredDate")
    @NotNull(message = "ExpiredDate must be provided")
    private LocalDateTime expiredDate;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;


    public static Integer getStatusInactive() {
        return Job.STATUS_INACTIVE;
    }
    public static Integer getStatusActive() {
        return  Job.STATUS_ACTIVE;
    }
}
