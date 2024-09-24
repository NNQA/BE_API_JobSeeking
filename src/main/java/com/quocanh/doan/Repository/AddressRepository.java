package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByProvinceNameAndDistrictNameAndCommuneNameAndLngAndLat(
      String provinceName, String districtName,String communeName, float lat,float lng
    );
}
