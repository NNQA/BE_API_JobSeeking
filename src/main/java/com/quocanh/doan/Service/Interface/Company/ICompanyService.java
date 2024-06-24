package com.quocanh.doan.Service.Interface.Company;

import com.quocanh.doan.Model.Company;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.dto.request.Company.CompanyRequest;
import org.springframework.validation.BindingResult;

public interface ICompanyService {

    void upToCompany(CompanyRequest companyRequest, UserPrincipal user, BindingResult bindingResult);
    Company save(Company company);
}
