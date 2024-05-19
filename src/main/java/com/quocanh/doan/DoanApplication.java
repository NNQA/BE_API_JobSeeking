package com.quocanh.doan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class DoanApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoanApplication.class, args);
    }

}
