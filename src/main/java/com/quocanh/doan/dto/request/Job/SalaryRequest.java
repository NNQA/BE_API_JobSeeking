package com.quocanh.doan.dto.request.Job;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryRequest {
    private Integer numberSort;

    private String value;
}
