package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.Enum.TokenType;
import com.quocanh.doan.Model.TokenStore;
import com.quocanh.doan.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenStoreRepository extends JpaRepository<TokenStore, Long> {
    Optional<TokenStore> findByTokenAndTokenTypeAndRevokedFalseAndExpiresAtAfter(String token, TokenType type, LocalDateTime expried);
    boolean existsByUserIdAndTokenTypeAndRevokedFalse(Long userId, TokenType tokenType);
    Optional<TokenStore> findByUser(User user);
    Optional<TokenStore> findTopByUserAndTokenTypeOrderByCreatedDateTimeDesc(User user, TokenType tokenType);
}
