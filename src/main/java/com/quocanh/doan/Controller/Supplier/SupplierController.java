package com.quocanh.doan.Controller.Supplier;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.Service.ImplementService.Company.CompanyImplementService;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.ImplementService.User.UserService;
import com.quocanh.doan.dto.request.Company.CompanyRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/supplier")
public class SupplierController {

    private final UserService userService;

    private final CompanyImplementService companyImplementService;
    public SupplierController(UserService userService, CompanyImplementService companyImplementService) {
        this.userService = userService;
        this.companyImplementService = companyImplementService;
    }
    @PostMapping("/upgradeRole")
    public ResponseEntity<?> HandleUpgradeRole(@RequestBody CompanyRequest companyRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.updateRoleUser(userPrincipal.getId());

        companyImplementService.createCompany(companyRequest, user);

        return ResponseEntity.ok().body(HttpStatus.OK);

    }

}
