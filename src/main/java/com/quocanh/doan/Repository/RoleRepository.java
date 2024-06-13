package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.ERole;
import com.quocanh.doan.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
