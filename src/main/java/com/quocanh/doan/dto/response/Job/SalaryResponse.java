package com.quocanh.doan.dto.response.Job;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryResponse {
    private Integer numberSort;

    private String value;
}
