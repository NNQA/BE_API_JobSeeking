package com.quocanh.doan.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.time.LocalDateTime;


@Entity
@Table(name = "address")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @FullTextField
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

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    public Address(String provinceName, String districtName, String communeName,String formattedAddressName, float lat, float lng) {
        this.provinceName = provinceName;
        this.districtName = districtName;
        this.communeName = communeName;
        this.formattedAddressName = formattedAddressName;
        this.lat = lat;
        this.lng = lng;
    }

    public Address() {

    }
}
