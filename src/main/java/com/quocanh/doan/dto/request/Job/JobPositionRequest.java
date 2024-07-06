package com.quocanh.doan.dto.request.Job;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class JobPositionRequest {
    @Pattern(regexp = "^[A-Za-z-, \\.\\/\\(\\)]{2,}$", message="Job type name is not valid.")
    @NotNull(message = "Job type name must be provided")
    private String jobPositionName;
}
