package com.quocanh.doan.Service.Interface.UserIntef;

import org.springframework.security.core.Authentication;

public interface IUserRefreshTokenService {
    Long saveTokenRequest(Authentication authentication);
}
