//package com.quocanh.doan.dbrunscript;
//
//import com.quocanh.doan.Model.Job;
//import com.quocanh.doan.Model.Salary;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.Query;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Service
//public class SalaryConversionService {
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Transactional
//    public void convertAndSaveSalaries() {
//        Query query = entityManager.createQuery("SELECT j FROM Job j", Salary.class);
//        for (int i = 1; i <= 20; i++) {
//           entityManager.merge(addSalary(i));
//        }
//    }
//
//
//    private Salary addSalary(int i) {
//        Salary salary1 = new Salary();
//        if(i % 2 == 0) {
//            salary1.setType(1);
//            salary1.setMaxSal(randomInt(100, 0));
//            salary1.setMinSal(randomInt(100, salary1.getMaxSal()));
//        }
//        else if(i % 3 == 0) {
//            salary1.setType(0);
//            salary1.setValue(randomInt(100,0));
//        } else {
//            salary1.setType(-1);
//        }
//        return salary1;
//    }
//
//    private Integer randomInt(int max, int i) {
//        Random rand = new Random();
//        if((max - i) > i && i != 0) return rand.nextInt(i);
//        return rand.nextInt(max);
//    }
//}