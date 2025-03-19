package com.quocanh.doan.config.CustomAuthenticationProvider;

import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.Signin.InvalidCredenticalException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class ExpandDaoAuthenticationProvider extends DaoAuthenticationProvider {

    public ExpandDaoAuthenticationProvider() {
        super();
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            super.additionalAuthenticationChecks(userDetails, authentication);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredenticalException(
                    "Login issue",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("login", "Invalid credential, Please check email or your password")
                    ),
                    "/system/auth"
            );
        }
    }
}
