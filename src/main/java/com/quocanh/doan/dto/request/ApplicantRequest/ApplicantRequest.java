package com.quocanh.doan.dto.request.ApplicantRequest;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicantRequest {
    private String jobId;

    private String resumeUrl;
}
