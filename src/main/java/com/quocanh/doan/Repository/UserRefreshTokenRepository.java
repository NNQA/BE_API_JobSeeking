package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {

}
