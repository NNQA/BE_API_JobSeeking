package com.quocanh.doan.Controller.ClientController;

import com.quocanh.doan.Service.ImplementService.ClientImplementService.ClientImplementService;
import com.quocanh.doan.dto.response.Job.JobResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/api/clientController")
public class ClientController {

    private final ClientImplementService clientImplementService;

    public ClientController(ClientImplementService clientImplementService) {
        this.clientImplementService = clientImplementService;
    }
    @GetMapping("/searchJob")
    public ResponseEntity<?> searchJob(
            @RequestParam(required = false, value = "title") String title,
            @RequestParam(required = false, value = "provinceName") String provinceName,
            @RequestParam(required = false,value = "cate") String cate,
            @RequestParam(required = false,value = "experience") String experience,
            @RequestParam(required = false,value = "type") String jobType,
            @RequestParam(required = false,value = "position") String level,
            @RequestParam(required = false,value = "salary") Integer salary
    ) {
        List<JobResponse> jobResponseList =
                clientImplementService
                        .searchJobRequestFromClient(title, provinceName, cate, experience,
                                jobType, level, salary);
        return ResponseEntity.ok(jobResponseList);
    }

    @GetMapping("/getAddressClient")
    public ResponseEntity<?> getAllProvinceName() {
        Set<String> listProvinceName = clientImplementService.getAllProvinceName();
        return ResponseEntity.ok(listProvinceName);
    }
    @GetMapping("/getCategoryName")
    public ResponseEntity<?> getCategoryName() {
        Set<String> listProvinceName = clientImplementService.getAllCategoryName();
        return ResponseEntity.ok(listProvinceName);
    }
    @GetMapping("/getDetailsJob")
    public ResponseEntity<?> getJobDetailsWithTitle(@RequestParam String title) {
        String str = java.net.URLDecoder.decode(title, StandardCharsets.UTF_8);
        JobResponse jobResponse= clientImplementService.getJobDetailsWithJob(str);
        return ResponseEntity.ok(jobResponse);
    }
}
