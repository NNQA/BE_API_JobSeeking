package com.quocanh.doan.Repository;

import com.quocanh.doan.Model.Enum.TokenType;
import com.quocanh.doan.Model.TokenStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenStoreRepository extends JpaRepository<TokenStore, Long> {
    Optional<TokenStore> findByTokenAndTokenTypeAndRevokedFalseAndExpiresAtAfter(String token, TokenType type, LocalDateTime expried);
    boolean existsByUserIdAndTokenTypeAndRevokedFalse(Long userId, TokenType tokenType);
}
