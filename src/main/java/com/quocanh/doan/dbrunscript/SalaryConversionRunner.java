//package com.quocanh.doan.dbrunscript;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SalaryConversionRunner implements CommandLineRunner {
//
//    private final SalaryConversionService salaryConversionService;
//
//    public SalaryConversionRunner(SalaryConversionService salaryConversionService) {
//        this.salaryConversionService = salaryConversionService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        salaryConversionService.convertAndSaveSalaries();
//        System.out.println("Salary conversion completed.");
//    }
//}