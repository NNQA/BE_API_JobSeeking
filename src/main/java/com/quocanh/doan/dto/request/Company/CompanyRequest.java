package com.quocanh.doan.dto.request.Company;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyRequest {
    String province;
    String district;
    String nameCompanny;
    String phone;
}
