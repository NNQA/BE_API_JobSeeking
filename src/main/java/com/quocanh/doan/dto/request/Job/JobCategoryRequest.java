package com.quocanh.doan.dto.request.Job;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobCategoryRequest {
    @NotNull(message = "Property must be provided")
    private String jobCategoryName;
}
