package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.Company;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUser(User user);
}
