package com.quocanh.doan.Service.Interface.Authentication;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.dto.request.authentication.LoginRequest;
import com.quocanh.doan.dto.request.authentication.ResetPasswordRequest;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import com.quocanh.doan.dto.response.LoginResponse;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IAuthenticationService {
    void signUp(SignupRequest signupRequest, BindingResult result);
    LoginResponse login(LoginRequest loginRequest, BindingResult bindingResult);
    void verifyEmailWithToken(String token);
    User updateRoleUser(Long id);
    void sendRequestEmailAgain(String email);
    void forgotPassword(String email);
    void resetPassword(ResetPasswordRequest request, BindingResult result);
}
