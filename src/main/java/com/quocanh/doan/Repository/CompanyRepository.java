package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
