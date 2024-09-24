package com.quocanh.doan.dto.request.Job;


import com.quocanh.doan.dto.request.AddressRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobRequest {
    @NotNull(message = "Title must be provided")
    private String title;

    @NotNull(message = "Description must be provided")
    private String description;

    @NotNull(message = "Position must be provided")
    private JobPositionRequest position;

    @NotNull(message = "Job type must be provided")
    private JobTypeRequest jobType;

    @NotNull(message = "Salary must be provided")
    private String salary;

    @NotNull(message = "ExpiredDate must be provided")
    private LocalDateTime expiredDate;

    private Set<SkillRequest> skills;

    @NotNull(message = "ExpiredDate must be provided")
    private Set<JobCategoryRequest> jobCategories;

    @NotNull(message = "Experience must be provided")
    private String experience;

    @NotNull(message = "Address must be provided.")
    private AddressRequest address;

}
