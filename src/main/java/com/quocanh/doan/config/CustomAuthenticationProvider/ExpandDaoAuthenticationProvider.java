package com.quocanh.doan.Security.CustomAuthenticationProvider;

import com.quocanh.doan.Exception.Signin.InvalidCredenticalException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class ExpandDaoAuthenticationProvider extends DaoAuthenticationProvider {

    public ExpandDaoAuthenticationProvider() {
        super();
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            super.additionalAuthenticationChecks(userDetails, authentication);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredenticalException("Invalid credential, Please check email or your password");
        }
    }
}
