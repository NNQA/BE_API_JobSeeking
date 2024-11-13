package com.quocanh.doan.dto.response.Job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quocanh.doan.Model.JobCategory;
import com.quocanh.doan.Model.Skill;
import com.quocanh.doan.dto.response.Address.AddressResponse;
import com.quocanh.doan.dto.response.CompanyResponse.CompanyResponse;
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
    JobTypeResponse type;
    List<Skill> skills;
    List<JobCategory> categories;
    SalaryResponse salary;
    String experience;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    LocalDateTime expiredDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    LocalDateTime createdDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    LocalDateTime updatedDateTime;
    AddressResponse address;
    CompanyResponse company;
}
