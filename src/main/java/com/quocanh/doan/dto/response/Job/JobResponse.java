package com.quocanh.doan.dto.response.Job;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobResponse {
    Long id;
    String title;
    String description;
    PositionResponse position;
    JobTypeResponse jobType;
    List<SkillReponse> skills;
    String salary;
    String experience;
    LocalDateTime expiredDate;
    LocalDateTime createdDateTime;
    LocalDateTime updatedDateTime;
}
