package com.quocanh.doan.dto.response.ApplicantResponse;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.quocanh.doan.dto.response.Job.JobResponse;
import com.quocanh.doan.dto.response.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicantResponse {
    Long id;
    JobResponse job;

    private String resumeUrl;
    UserResponse user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    LocalDateTime createdDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    LocalDateTime updatedDateTime;
}
