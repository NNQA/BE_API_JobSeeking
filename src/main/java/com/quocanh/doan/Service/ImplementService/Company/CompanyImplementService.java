package com.quocanh.doan.Service.ImplementService.Company;

import com.quocanh.doan.Exception.Company.CompanyExeptionHanlde;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Mapper.CompanyMapper;
import com.quocanh.doan.Model.Address;
import com.quocanh.doan.Model.Company;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.AddressRepository;
import com.quocanh.doan.Repository.CompanyRepository;
import com.quocanh.doan.Repository.UserRepository;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.ImplementService.User.UserService;
import com.quocanh.doan.Service.Interface.Company.ICompanyService;
import com.quocanh.doan.dto.request.Company.CompanyRequest;
import com.quocanh.doan.dto.response.CompanyResponse.CompanyResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyImplementService implements ICompanyService {
    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    public  CompanyImplementService(CompanyRepository companyRepository, UserService userService,
                                    AddressRepository addressRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }
    private String[] createMessageError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).toArray(String[]::new);
    }


    public Company save(Company company) {
        return this.companyRepository.save(company);
    }

    @Override
    public void updateCompany(CompanyRequest companyRequest, UserPrincipal userPrincipal, BindingResult bindingResult) {
        logger.info("############## service is working");
        if(bindingResult.hasErrors()) {
            logger.info("---------------- Validation has errors - " + createMessageError(bindingResult));
            throw new CompanyExeptionHanlde(createMessageError(bindingResult)[0]);
        }
        if(Objects.isNull(companyRequest)) {
            logger.info("---------------- Company is null");
            throw new CompanyExeptionHanlde("Company is null");
        }

        try {
            Address address = addressRepository.findByProvinceNameAndDistrictNameAndCommuneNameAndLngAndLat(
                            companyRequest.getAddress().getProvinceName(),
                            companyRequest.getAddress().getDistrictName(), companyRequest.getAddress().getCommuneName()
                            , companyRequest.getAddress().getLng(),
                            companyRequest.getAddress().getLat()
                    )
                    .orElseGet(
                            () -> {
                                Address address1 = new Address(
                                        companyRequest.getAddress().getProvinceName(),
                                        companyRequest.getAddress().getDistrictName(), companyRequest.getAddress().getCommuneName(),
                                        companyRequest.getAddress().getFormattedAddressName()
                                        , companyRequest.getAddress().getLng(),
                                        companyRequest.getAddress().getLat()
                                );
                                return this.addressRepository.save(address1);
                            }
                    );
            logger.info("---------------- Update details company");
            User user = this.userRepository.findById(userPrincipal.getId())
                    .orElseThrow(
                            () -> new UserNotFoundException("Cannot found user in our system")
                    );

            Company company = companyRepository.findByUser(user).orElseThrow(
                    () -> new CompanyExeptionHanlde("Cannot find company in our system")
            );
            if(companyRequest.getNameCompany() != null
                    && companyRequest.getPhone() != null
                        && companyRequest.getNumberEmp() != null) {
                company.setNameCompany(companyRequest.getNameCompany());
                company.setPhone(companyRequest.getPhone());
                company.setNumberEmp(companyRequest.getNumberEmp());
            }

            if(companyRequest.getBusinessRegistrationNumber() != null
                    && companyRequest.getBusinessType() != null) {
                company.setBusinessRegistrationNumber(companyRequest.getBusinessRegistrationNumber());
                company.setBusinessType(companyRequest.getBusinessType());
            }

            if(companyRequest.getDescription() != null
                    && companyRequest.getLinkComp() != null) {
                company.setDescription(companyRequest.getDescription());
                company.setLinkComp(companyRequest.getLinkComp());
            }
            company.setAddress(address);
            this.save(company);
            logger.info("---------------- Finished Ok");
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw  new CompanyExeptionHanlde(e.getMessage());
        }

    }

    @Override
    public CompanyResponse getCurrentCompany(Long idUser) {
        logger.info("############## service is working");
        User user = this.userRepository.findById(idUser)
                .orElseThrow(
                        () -> new UserNotFoundException("Cannot found user in our system")
                );
        Company company = this.companyRepository.findByUser(user)
                .orElseThrow(
                        () -> new CompanyExeptionHanlde("Cannot find company or company not exist in our system")
                );
        CompanyResponse companyResponse = CompanyMapper.INSTANCE.companyToResponse(company);
        return companyResponse;
    }

    @Override
    public void upToCompany(CompanyRequest companyRequest, UserPrincipal userPrincipal, BindingResult bindingResult) {
        logger.info("############## service is working");
        if(bindingResult.hasErrors()) {
            logger.info("---------------- Validation has errors - " + createMessageError(bindingResult));
            throw new CompanyExeptionHanlde(createMessageError(bindingResult)[0]);
        }
        if(Objects.isNull(companyRequest)) {
            logger.info("---------------- Company is null");
            throw new CompanyExeptionHanlde("Company is null");
        }
        try {
            Address address = addressRepository.findByProvinceNameAndDistrictNameAndCommuneNameAndLngAndLat(
                            companyRequest.getAddress().getProvinceName(),
                            companyRequest.getAddress().getDistrictName(), companyRequest.getAddress().getCommuneName()
                            , companyRequest.getAddress().getLng(),
                            companyRequest.getAddress().getLat()
                    )
                    .orElseGet(
                            () -> {
                                Address address1 = new Address(
                                        companyRequest.getAddress().getProvinceName(),
                                        companyRequest.getAddress().getDistrictName(), companyRequest.getAddress().getCommuneName(),
                                        companyRequest.getAddress().getFormattedAddressName()
                                        , companyRequest.getAddress().getLng(),
                                        companyRequest.getAddress().getLat()
                                );
                                return this.addressRepository.save(address1);
                            }
                    );
            logger.info("---------------- Update role user");
            User user = userService.updateRoleUser(userPrincipal.getId());
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
