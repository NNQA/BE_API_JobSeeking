package com.quocanh.doan.dto.request.Job;

import com.quocanh.doan.Model.JobPosition;
import com.quocanh.doan.Model.JobType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    private JobTypeRequest jobTypeRequest;

    @Min(value = 0, message = "Price must be {value} or higher")
    @NotNull(message = "Salary must be provided")
    private Double salary;

    @NotNull(message = "Skill requests must be provided")
    @Size(min = 1, max = 10, message = "There must be between 1 and 10 skills")
    Set<SkillRequest> skillRequests = new HashSet<>();
    @NotNull(message = "ActiveDate must be provided")
    private LocalDateTime activeDate;

    @NotNull(message = "ExpiredDate must be provided")
    private LocalDateTime expiredDate;

}
