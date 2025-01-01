package com.quocanh.doan.Mapper;


import com.quocanh.doan.Model.Company;
import com.quocanh.doan.dto.response.CompanyResponse.CompanyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyResponse companyToResponse(Company company);
    List<CompanyResponse> companyListToAppicantResponseList(List<Company> applicants);
}
