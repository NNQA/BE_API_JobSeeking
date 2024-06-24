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

    @Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message="City name is not valid.")
    @NotNull(message = "City name must be provided.")
    String cityName;

    @NotNull (message = "Longitude must be provided.")
    @Min(value=-180, message = "Longitude  must be {value} or higher!")
    @Max(value=180, message = "Longitude must be {value} or lower!")
    Double longitude;

    @NotNull (message = "Latitude must be provided.")
    @Min(value=-90, message = "Latitude  must be {value} or higher!")
    @Max(value=90, message = "Latitude must be {value} or lower!")
    Double latitude;
}
