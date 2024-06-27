package com.quocanh.doan.Controller.authentication;


import com.quocanh.doan.Exception.Signin.InvalidCredenticalException;
import com.quocanh.doan.Security.Jwt.TokenProvider;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.ImplementService.User.UserRefreshTokenService;
import com.quocanh.doan.Service.ImplementService.User.UserService;
import com.quocanh.doan.dto.request.ApiResponseProblemDetails;
import com.quocanh.doan.dto.request.authentication.CodeRequest;
import com.quocanh.doan.dto.request.authentication.LoginRequest;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import com.quocanh.doan.dto.response.LoginResponse;
import com.quocanh.doan.dto.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    private final UserService userService;
    private final UserRefreshTokenService userRefreshTokenService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserService userService,TokenProvider provider,
                                    UserRefreshTokenService userRefreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = provider;
        this.userService = userService;
        this.userRefreshTokenService = userRefreshTokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody SignupRequest signupRequest) {
        userService.signUp(signupRequest);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("############## /api/auth/login started");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = tokenProvider.generateToken(authentication);
            Long refreshToken = userRefreshTokenService.saveTokenRequest(authentication);

            return ResponseEntity.ok().body(
                    new LoginResponse(
                            accessToken,
                            String.valueOf(refreshToken)
                    )
            );
        } catch (AuthenticationException exception) {
            logger.info("-------------- increnditails login");
            throw new InvalidCredenticalException("Some attributes is not match");
        }

    }

    @PostMapping("/verifycode")
    public  ResponseEntity VerifiedCode(@RequestBody CodeRequest codeRequest) {
        userService.VerifiedCode(codeRequest.getEmail(), codeRequest.getCode());
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
