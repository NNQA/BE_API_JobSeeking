package com.quocanh.doan.dto.response.Address;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    protected String provinceName;

    protected String communeName;

    protected String formattedAddressName;

    protected float lat;

    protected float lng;
    protected String districtName;
}
