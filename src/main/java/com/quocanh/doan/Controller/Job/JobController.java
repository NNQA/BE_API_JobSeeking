package com.quocanh.doan.Controller.Job;


import com.quocanh.doan.Service.ImplementService.Job.JobImplement;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.dto.request.Company.CompanyRequest;
import com.quocanh.doan.dto.request.Job.JobRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/job")
public class JobController {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private final JobImplement jobImplement;

    public JobController(JobImplement jobImplement) {
        this.jobImplement = jobImplement;
    }

    @PostMapping("/addNewJob")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<?> handleUpgradeRole(@Valid @RequestBody JobRequest request, BindingResult bindingResult) {
        logger.info("############## /api/job/addNewPost started");
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        jobImplement.addNewJob(request,userPrincipal, bindingResult);

        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
