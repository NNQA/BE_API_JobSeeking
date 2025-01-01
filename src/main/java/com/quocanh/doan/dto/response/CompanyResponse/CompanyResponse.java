package com.quocanh.doan.dto.response.CompanyResponse;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.quocanh.doan.dto.response.Address.AddressResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyResponse {
    String phone;
    String nameCompany;
    AddressResponse address;
    String linkComp;
    String numberEmp;
    String businessType;
    String description;
    String businessRegistrationNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    LocalDateTime createdDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    LocalDateTime updatedDateTime;
}
