package com.quocanh.doan.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {

    @NotNull(message = "Province name must be provided.")
    protected String provinceName;

    @NotNull(message = "District name must be provided.")
    protected String districtName;

    @NotNull(message = "Commune name must be provided.")
    protected String communeName;

    @NotNull(message = "Formatted address name must be provided.")
    protected String formattedAddressName;

    @NotNull(message = "Lat must be provided.")
    protected float lat;

    @NotNull(message = "Lng must be provided.")
    protected float lng;

}
