package com.quocanh.doan.Service.ImplementService.Company;

import com.quocanh.doan.Model.Company;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.CompanyRepository;
import com.quocanh.doan.Service.Interface.Company.ICompanyService;
import com.quocanh.doan.dto.request.Company.CompanyRequest;
import com.quocanh.doan.dto.request.authentication.CodeRequest;
import org.springframework.stereotype.Service;

@Service
public class CompanyImplementService implements ICompanyService {
    private final CompanyRepository companyRepository;

    public  CompanyImplementService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company save(Company company) {
        return this.companyRepository.save(company);
    }
    @Override
    public void createCompany(CompanyRequest companyRequest, User user) {
        Company company = new Company();
        company.setPhone(companyRequest.getPhone());
        company.setUser(user);
        company.setProvince(companyRequest.getProvince());
        company.setDistrict(companyRequest.getDistrict());
        company.setNameComany(companyRequest.getNameCompanny());
        this.save(company);
    }
}
