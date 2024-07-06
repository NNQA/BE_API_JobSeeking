package com.quocanh.doan.dto.request.Job;

import com.quocanh.doan.Model.JobPosition;
import com.quocanh.doan.Model.JobType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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

    @NotNull(message = "ActiveDate must be provided")
    private LocalDateTime activeDate;

    @NotNull(message = "ExpiredDate must be provided")
    private LocalDateTime expiredDate;
}
