package com.quocanh.doan.Controller.Job;


import com.quocanh.doan.Model.Job;
import com.quocanh.doan.Service.ImplementService.Job.JobImplement;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Utils.AppConstants;
import com.quocanh.doan.dto.request.Company.CompanyRequest;
import com.quocanh.doan.dto.request.Job.JobRequest;
import com.quocanh.doan.dto.response.Job.JobPaginationResponse;
import com.quocanh.doan.dto.response.Job.JobResponse;
import com.quocanh.doan.dto.response.Job.JobTypeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> addNewJob(@Valid @RequestBody JobRequest request, BindingResult bindingResult) {
        logger.info("############## /api/job/addNewPost started");
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobImplement.addNewJob(request,userPrincipal, bindingResult);

        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/getAllJob")
    public ResponseEntity<?> getAllJob() {
        logger.info("############## /api/job/getAllHJob started");

        List<Job> jobList = jobImplement.getAllJob();

        return ResponseEntity.ok().body(
                jobList
        );
    }
    @GetMapping("/getDetailJob/{id}")
    public ResponseEntity<?> getDetailJob(@PathVariable Long id) {
        logger.info("############## /api/job/getDetailJob started");

        Job jobTypeResponse = jobImplement.getById(id);

        return ResponseEntity.ok().body(
                jobTypeResponse
        );
    }
    @GetMapping("/getDetailJobForCompany/{id}")
    public ResponseEntity<?> getDetailJobForCompany(@PathVariable Long id) {
        logger.info("############## /api/job/getDetailJob started");
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobResponse jobTypeResponse = jobImplement.getByIdForCompany(id, userPrincipal.getId());
        System.out.println(jobTypeResponse);
        return ResponseEntity.ok().body(
                jobTypeResponse
        );
    }
    @PutMapping("/udpateDetailJobForCompany/{id}")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<?> updateDetailJobForCompany(@PathVariable Long id,@Valid @RequestBody JobRequest request, BindingResult bindingResult) {
        logger.info("############## /api/job/updateDetailJob started");
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobImplement.updateJob(id,request, userPrincipal, bindingResult);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    @GetMapping("/getJobPagination")
    public ResponseEntity<?> getPaginationAllProduct(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_CURRENT, required = false) Integer pageNo,
            @RequestParam(value = "size", defaultValue =AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize

    ) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobPaginationResponse jobPaginationResponse = jobImplement.getAllJobPage(userPrincipal.getId(), pageNo - 1, pageSize);
        System.out.println(jobPaginationResponse);
        return ResponseEntity.ok(jobPaginationResponse);

    }

    @DeleteMapping("/deleteDetailJobForCompany/{id}")
    @PreAuthorize("hasRole('ROLE_SUPPLIER')")
    public ResponseEntity<?> deleteDetailJobForCompany(@PathVariable Long id) {
        logger.info("############## /api/job/deleteDetailJob started");

        this.jobImplement.DeleteById(id);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

}
