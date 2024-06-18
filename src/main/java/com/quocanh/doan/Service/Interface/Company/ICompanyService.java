package com.quocanh.doan.Service.Interface.Company;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.dto.request.Company.CompanyRequest;

public interface ICompanyService {

    void createCompany(CompanyRequest companyRequest, User user);
}
