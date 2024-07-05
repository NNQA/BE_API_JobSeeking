package com.quocanh.doan.Service.ImplementService.Company;

import com.quocanh.doan.Exception.Company.CompanyExeptionHanlde;
import com.quocanh.doan.Model.Address;
import com.quocanh.doan.Model.Company;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.AddressRepository;
import com.quocanh.doan.Repository.CompanyRepository;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.ImplementService.User.UserService;
import com.quocanh.doan.Service.Interface.Company.ICompanyService;
import com.quocanh.doan.dto.request.Company.CompanyRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CompanyImplementService implements ICompanyService {
    private final CompanyRepository companyRepository;
    private final UserService userService;

    private final AddressRepository addressRepository;

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    public  CompanyImplementService(CompanyRepository companyRepository, UserService userService, AddressRepository addressRepository) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.addressRepository = addressRepository;
    }
    private String createMessageError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
    }
    public Company save(Company company) {
        return this.companyRepository.save(company);
    }
    @Override
    public void upToCompany(CompanyRequest companyRequest, UserPrincipal userPrincipal, BindingResult bindingResult) {
        logger.info("############## service is working");
        if(bindingResult.hasErrors()) {
            logger.info("---------------- Validation has errors - " + createMessageError(bindingResult));
            throw new CompanyExeptionHanlde(createMessageError(bindingResult));
        }
        if(Objects.isNull(companyRequest)) {
            logger.info("---------------- Company is null");
            throw new CompanyExeptionHanlde("Company is null");
        }
        try {
            logger.info("---------------- Update role user");
            User user = userService.updateRoleUser(userPrincipal.getId());
            System.out.println(companyRequest);
            Address address = this.addressRepository.save(new Address(companyRequest.getAddress().getProvinceName(),companyRequest.getAddress().getDistrictName()));
            Company company = new Company(
                    user,
                    address,
                    companyRequest.getNameCompany(),
                    companyRequest.getPhone()
            );
            logger.info("---------------- Create new company");

            this.save(company);
            logger.info("---------------- Finished Ok");
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw  new CompanyExeptionHanlde(e.getMessage());
        }

    }
}
