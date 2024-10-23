package com.quocanh.doan.Controller.ClientController;

import com.quocanh.doan.Service.ImplementService.ClientImplementService.ClientImplementService;
import com.quocanh.doan.dto.response.Job.JobResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
            @RequestParam(required = false,value = "cate") String cate
    ) {
        System.out.println(title);
        List<JobResponse> jobResponseList = clientImplementService.searchJobRequestFromClient(title);
        return ResponseEntity.ok(jobResponseList);
    }
}
