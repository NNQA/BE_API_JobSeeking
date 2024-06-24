package com.quocanh.doan.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "address")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Pattern(regexp = "^[a-zA-Z]{2,}+( [a-zA-Z_]+)*$", message="City name is not valid.")
    @NotNull(message = "City name must be provided.")
    protected String cityName;

    @Column(name="longitude")
    @NotNull (message = "Longitude must be provided.")
    @Min(value=-180, message = "Longitude  must be {value} or higher!")
    @Max(value=180, message = "Longitude must be {value} or lower!")
    private Double longitude;

    @Column(name="latitude")
    @NotNull (message = "Latitude must be provided.")
    @Min(value=-90, message = "Latitude  must be {value} or higher!")
    @Max(value=90, message = "Latitude must be {value} or lower!")
    private Double latitude;

    public Address(String cityName, Double longitude, Double latitude) {
        this.cityName = cityName;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
