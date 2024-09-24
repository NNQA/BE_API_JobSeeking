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
    PositionResponse positionResponse;
    JobTypeResponse jobTypeResponse;
    List<SkillReponse> skillReponses;
    Double salary;
    LocalDateTime expiredDate;
    LocalDateTime activeDate;
    LocalDateTime createdDateTime;
    LocalDateTime updatedDateTime;
}
