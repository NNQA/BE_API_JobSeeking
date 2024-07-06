package com.quocanh.doan.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Job")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
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

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "position")
    @NotNull(message = "Position must be provided")
    private JobPosition position;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employer")
    @NotNull(message = "Employer must be provided")
    private Company company;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    @NotNull(message = "JobType must be provided")
    private JobType type;


    @JoinColumn(name = "salary")
    @NotNull(message = "Salary must be provided")
    @Min(value = 0, message = "Price must be {value} or higher")
    private Double salary;

    @JoinColumn(name = "activeDate")
    @NotNull(message = "ActiveDate must be provided")
    private LocalDateTime activeDate;

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
