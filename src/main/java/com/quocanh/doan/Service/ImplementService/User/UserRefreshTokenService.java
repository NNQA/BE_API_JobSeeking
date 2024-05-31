package com.quocanh.doan.Service.ImplementService.User;

import com.quocanh.doan.Model.UserRefreshToken;
import com.quocanh.doan.Repository.UserRefreshTokenRepository;
import com.quocanh.doan.Service.Interface.UserIntef.IUserRefreshTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class UserRefreshTokenService implements IUserRefreshTokenService {

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    public  UserRefreshTokenService(UserRefreshTokenRepository userRefreshTokenRepository) {
        this.userRefreshTokenRepository = userRefreshTokenRepository;
    }
    @Override
    public Long saveTokenRequest(Authentication authentication) {
        Duration tokenValidityDuration = Duration.ofDays(30);
        UserRefreshToken userRefreshToken = new UserRefreshToken();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        userRefreshToken.setUserId(userPrincipal.getId());

        userRefreshToken.setExpiryTime(Instant.now().plus(tokenValidityDuration));

        userRefreshTokenRepository.save(userRefreshToken);

        return userRefreshToken.getId();
    }
}
