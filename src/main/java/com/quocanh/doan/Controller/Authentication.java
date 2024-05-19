package com.quocanh.doan.Controller;


import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class Authentication {


//    @PostMapping("/authorize")
//    public String Login() {
//        System.out.println("asd");
//        return "asd";
//    }
    @GetMapping("/login")
    public ResponseEntity<?> getLogin() {
        return ResponseEntity.ok("Asdasd");
    }
}
