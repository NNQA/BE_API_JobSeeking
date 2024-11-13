package com.quocanh.doan.dto.response.CompanyResponse;


import com.quocanh.doan.dto.response.Address.AddressResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyResponse {
    private String phone;

    private String nameCompany;
    private AddressResponse address;
}
