package com.quocanh.doan.Controller.authentication;


import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.Signin.InvalidCredenticalException;
import com.quocanh.doan.Service.Interface.Authentication.IAuthenticationService;
import com.quocanh.doan.config.Jwt.TokenProvider;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.ImplementService.User.UserRefreshTokenService;
import com.quocanh.doan.Service.ImplementService.User.UserService;
import com.quocanh.doan.dto.request.ApiResponseProblemDetails;
import com.quocanh.doan.dto.request.authentication.*;
import com.quocanh.doan.dto.response.LoginResponse;
import com.quocanh.doan.dto.response.ModelReponseSuccess;
import com.quocanh.doan.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final IAuthenticationService iAuthenticationService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    public ModelReponseSuccess responseSuccess(Object body, String message) {
        return ModelReponseSuccess.builder()
                .message(message)
                .body(body)
                .build();
    }
    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody @Valid SignupRequest signupRequest, BindingResult bindingResult) {
        logger.info("############## Authentication Controller /api/auth/signup started");
        iAuthenticationService.signUp(signupRequest, bindingResult);
        return ResponseEntity.ok().body(
                responseSuccess(null, "Sign up successfully")
        );
    }
    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody @Valid  LoginRequest loginRequest, BindingResult bindingResult) {
        logger.info("############## Authentication Controller /api/auth/login started");
        return ResponseEntity.ok().body(
                responseSuccess(iAuthenticationService.login(loginRequest, bindingResult), "Log in successfully")
        );
    }

    @PostMapping("/verify-email")
    public  ResponseEntity VerifiedCode(@RequestBody VerifyEmailRequest request) {
        logger.info("############## Authentication Controller /api/auth/verify-email started");
        iAuthenticationService.verifyEmailWithToken(request.getToken());
        return ResponseEntity.ok().body(
                responseSuccess(null, "VerifyEmail is successfully")
        );
    }

    @PostMapping("/request-token-email")
    public  ResponseEntity requestTokenEmailAgain(@RequestBody RequestTokenEmailAgain request) {
        logger.info("############## Authentication Controller /api/auth/request-token-email started");
        iAuthenticationService.sendRequestEmailAgain(request.getEmail());
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public  ResponseEntity forgotPasswordHandle(@RequestBody ForgotPasswordRequest request) {
        logger.info("############## Authentication Controller /api/auth/forgot-password started");
        iAuthenticationService.forgotPassword(request.getEmail());
        return ResponseEntity.ok().body(
                responseSuccess(null, "Forgot password is successfully")
        );
    }
    @PostMapping("/is-new-password")
    public  ResponseEntity requestNewPassword(@RequestBody @Valid ResetPasswordRequest request, BindingResult result) {
        logger.info("############## Authentication Controller /api/auth/is-new-password started");
        iAuthenticationService.resetPassword(request,result);
        return ResponseEntity.ok().body(
                responseSuccess(null, "Your requested password is successfully")
        );
    }
}
