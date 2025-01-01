package com.quocanh.doan.Controller.Admin;

import com.quocanh.doan.Service.ImplementService.IndexService.IndexService;
import com.quocanh.doan.Service.ImplementService.Job.JobImplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final IndexService indexService;
    private final JobImplement jobImplement;
    private final Logger logger = (Logger) LoggerFactory.getLogger(AdminController.class);

    public AdminController(IndexService indexService, JobImplement jobImplement) {
        this.jobImplement = jobImplement;
        this.indexService = indexService;
    }

    @PostMapping("/mark-index")
    public ResponseEntity<?> reindexJobs() {
        try {
            logger.info("#### mark-index api's working");
            indexService.rebuildIndex();
            return ResponseEntity.ok("Re-build index initiated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during re-indexing: " + e.getMessage());
        }
    }

    @PutMapping("/approve-job/{id}")
    public ResponseEntity<?> approveJobController(@PathVariable Long id) {
        logger.info("#### approve-job api's working");
        jobImplement.approvedJob(id);
        return ResponseEntity.ok("Approving active job successfully.");
    }
}
