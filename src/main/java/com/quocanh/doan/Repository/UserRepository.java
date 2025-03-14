package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findUserByEmailOrUserName(String email, String name);
}
