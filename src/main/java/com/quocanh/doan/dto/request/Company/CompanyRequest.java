package com.quocanh.doan.dto.request.Company;

import com.quocanh.doan.dto.request.AddressRequest;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyRequest {
    @NotNull(message = "Phone number must be provided.")
    @Pattern(regexp = "^(((\\+|)84)|0)(3|5|7|8|9)+([0-9]{8})$", message="Mobile phone number is not valid.")
    private String phone;

    @NotNull(message = "Company name must be provided.")
    private String nameCompany;

    @NotNull(message = "Address must be provided.")
    private AddressRequest address;
}
