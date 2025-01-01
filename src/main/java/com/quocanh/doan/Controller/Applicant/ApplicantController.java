package com.quocanh.doan.Controller.Applicant;

import com.quocanh.doan.Service.ImplementService.ApplicantService.ApplicantService;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Utils.AppConstants;
import com.quocanh.doan.dto.request.ApplicantRequest.ApplicantRequest;
import com.quocanh.doan.dto.response.ApplicantResponse.ApplicantPaginationResponse;
import com.quocanh.doan.dto.response.ApplicantResponse.ApplicantResponse;
import com.quocanh.doan.dto.response.ApplicantResponse.IsCheckApplied;
import com.quocanh.doan.dto.response.Job.JobPaginationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/api/applicant")
public class ApplicantController {

    private final ApplicantService applicantService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }
    @PostMapping("/createApplicant")
    public ResponseEntity<?> CreateNewApplicant(@RequestBody ApplicantRequest applicantRequest) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        applicantService.addApplicatnt(applicantRequest, userPrincipal.getId());
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/checkIsApply/{id}")
    public ResponseEntity<?> CheckIsApplyOfUser(@PathVariable Long id) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IsCheckApplied isCheckApplied = new IsCheckApplied(applicantService.isCheckApplied(userPrincipal.getId(),id));
        return ResponseEntity.ok().body(isCheckApplied);
    }

    @GetMapping("/getApplicant")
    public ResponseEntity<?> CheckIsApplyOfUser(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_CURRENT, required = false) Integer pageNo,
            @RequestParam(value = "size", defaultValue =AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize
    ) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.applicantService.getAllApplicant(userPrincipal.getId(), pageSize, pageNo);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/getApplicantPagination")
    public ResponseEntity<?> getPaginationAllProduct(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_CURRENT, required = false) Integer pageNo,
            @RequestParam(value = "size", defaultValue =AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize

    ) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicantPaginationResponse applicantPaginationResponse = applicantService.getAllApplicant(userPrincipal.getId(), pageNo - 1, pageSize);
        return ResponseEntity.ok(applicantPaginationResponse);
    }

    @GetMapping("/getDetailsApplicantUser/{id1}/{username}")
    public ResponseEntity<?> getDetailsApplicantUser(@PathVariable("id1") Long id1,
                                                     @PathVariable("username") String username) {
        logger.info("############## /api/applicant/getDetailsApplicantUser started");
        String str = java.net.URLDecoder.decode(username, StandardCharsets.UTF_8);
        ApplicantResponse applicantResponse = applicantService.getDetailsApplicantUser(id1, str);
        return ResponseEntity.ok(applicantResponse);
    }
}
