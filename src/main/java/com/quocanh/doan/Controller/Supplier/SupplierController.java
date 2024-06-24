package com.quocanh.doan.Controller.Supplier;

import com.quocanh.doan.Service.ImplementService.Company.CompanyImplementService;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.ImplementService.User.UserService;
import com.quocanh.doan.dto.request.Company.CompanyRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/supplier")
public class SupplierController {

    private final UserService userService;

    private final CompanyImplementService companyImplementService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    public SupplierController(UserService userService, CompanyImplementService companyImplementService) {
        this.userService = userService;
        this.companyImplementService = companyImplementService;
    }
    @PostMapping("/upgradeRole")
    public ResponseEntity<?> handleUpgradeRole(@Valid @RequestBody CompanyRequest companyRequest, BindingResult bindingResult) {
        logger.info("############## /api/supplier/upgradeRole started");
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        companyImplementService.upToCompany(companyRequest, userPrincipal, bindingResult);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }


}
