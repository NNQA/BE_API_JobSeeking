package com.quocanh.doan.Service.ImplementService.Authentication;

import com.quocanh.doan.Exception.CheckCode.EmailVerifycationException;
import com.quocanh.doan.Exception.CheckCode.ResetPasswordException;
import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.Signin.InvalidCredenticalException;
import com.quocanh.doan.Exception.Authentication.EmailExeption;
import com.quocanh.doan.Exception.Authentication.AuthenticationException;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Model.AuthProvider;
import com.quocanh.doan.Model.Enum.ERole;
import com.quocanh.doan.Model.Enum.TokenType;
import com.quocanh.doan.Model.Role;
import com.quocanh.doan.Model.TokenStore;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.RoleRepository;
import com.quocanh.doan.Repository.TokenStoreRepository;
import com.quocanh.doan.Repository.UserRepository;
import com.quocanh.doan.Service.ImplementService.Email.EmailImplementService;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.ImplementService.User.UserRefreshTokenService;
import com.quocanh.doan.Service.Interface.Authentication.IAuthenticationService;
import com.quocanh.doan.Service.Interface.UserIntef.IUserProfileService;
import com.quocanh.doan.config.Jwt.TokenProvider;
import com.quocanh.doan.dto.request.authentication.LoginRequest;
import com.quocanh.doan.dto.request.authentication.ResetPasswordRequest;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import com.quocanh.doan.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationImplementService implements IAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final EmailImplementService emailImplementService;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private final TokenStoreRepository tokenStoreRepository;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenService userRefreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final IUserProfileService iUserProfileService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public User save(User user) {
        userRepository.save(user);
        return user;
    }
    public User findUserWithEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User account",
                                HttpStatus.NOT_FOUND,
                                List.of(
                                        new ErrorDetail("email", "Cannot find your email or account system")
                                ),
                                "/system/user"
                        )
                );
        return user;
    }
    private User saveUser(SignupRequest signupRequest) {
        return User.builder()
                .email(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword()))
                .provider(AuthProvider.local)
                .verifiedEmail(false)
                .roles(Set.of(
                        roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new AuthenticationException(
                                        "Missing property",
                                        HttpStatus.BAD_REQUEST,
                                        List.of(
                                                new ErrorDetail("Roles", "Roles dont found in my system!!!")
                                        ),
                                        "/system/signup"
                                ))
                ))
                .isNewUser(true)
                .build();
    }
    private static UserPrincipal getUserPrincipal(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if(!userPrincipal.getVerifiedEmail()) {
            throw new EmailVerifycationException(
                    "Verify email issue",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("email", "Your account is not verified email. Please check email!!!")
                    ),
                    "/system/verify"
            );
        }
        return userPrincipal;
    }
    private TokenStore saveToken(User user, String verifycationToken, TokenType type) {
        return TokenStore.builder()
                .revoked(false)
                .token(verifycationToken)
                .tokenType(type)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
    }
    @Override
    public void signUp(SignupRequest signupRequest, BindingResult result) {
        logger.info("############## Authentication Service /api/auth/signup started");
        if (result.hasErrors()) {
            logger.info("------------ validation error: " + result.getAllErrors().size());

            List<ErrorDetail> errors = result.getFieldErrors().stream()
                    .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                    .toList();

            throw new AuthenticationException(
                    "Missing property",
                    HttpStatus.BAD_REQUEST,
                    errors,
                    "/system/signup"
            );
        }
        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailExeption(
                    "Email existed",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("email", "Your email is existed!!!")
                    ),
                    "/system/signup"
            );
        }
        logger.info("################ finished validation");

        String verificationToken = tokenProvider.generateTokenWithEmail(signupRequest.getEmail());
        User user = saveUser(signupRequest);
        TokenStore tokenStore = saveToken(user, verificationToken, TokenType.EMAIL_VERIFYCATION);

        this.save(user);
        iUserProfileService.CreateUserProfileUser(user.getId());
        tokenStoreRepository.save(tokenStore);
        emailImplementService.sendMailRegister(signupRequest.getEmail(), verificationToken);

        logger.info("############## Authentication Service /api/auth/signup finish");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest, BindingResult bindingResult) {
        try {
            logger.info("############## Authentication Service /api/auth/login started");
            if (bindingResult.hasErrors()) {
                logger.info("------------ validation error: " + bindingResult.getAllErrors().size());

                List<ErrorDetail> errors = bindingResult.getFieldErrors().stream()
                        .map(e -> new ErrorDetail(e.getField(), e.getDefaultMessage()))
                        .toList();

                throw new AuthenticationException(
                        "Missing property",
                        HttpStatus.BAD_REQUEST,
                        errors,
                        "/system/login"
                );
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal userPrincipal = getUserPrincipal(authentication);
            String accessToken = tokenProvider.generateToken(authentication);
            Long refreshToken = userRefreshTokenService.saveTokenRequest(authentication);

            return LoginResponse.builder()
                    .isNewUser(userPrincipal.getIsNewUser())
                    .authorities(userPrincipal.getAuthorities())
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .build();
        } catch (org.springframework.security.core.AuthenticationException exception) {
            logger.info("-------------- invalid login");
            throw new InvalidCredenticalException(
                    "Login issue",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("login", exception.getMessage())
                    ),
                    "/system/auth/login"
            );
        }
    }

    @Override
    public void verifyEmailWithToken(String token) {
        logger.info("############## Authentication Service /api/auth/verify-email started");
        TokenStore tokenStore = tokenStoreRepository.findByTokenAndTokenTypeAndRevokedFalseAndExpiresAtAfter(
                token, TokenType.EMAIL_VERIFYCATION, LocalDateTime.now()
        ).orElseThrow(() ->
                new EmailVerifycationException(
                        "Verify email issue",
                        HttpStatus.BAD_REQUEST,
                        List.of(
                                new ErrorDetail("email", "Your token is expired or cannot find your token!!!")
                        ),
                        "/system/verify"
                )
        );
        User user = tokenStore.getUser();
        tokenStore.setRevoked(true);
        user.setVerifiedEmail(true);
        this.save(user);
        tokenStoreRepository.save(tokenStore);
        logger.info("############## Authentication Controller /api/auth/verify-email finished");
    }
    @Override
    public User updateRoleUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with " + id)
        );
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new AuthenticationException("Error: Role is not found."));
        Role supplierRole = roleRepository.findByName(ERole.ROLE_SUPPLIER)
                .orElseThrow(() -> new AuthenticationException("Error: Role is not found."));
        roles.add(supplierRole);
        roles.add(userRole);
        user.setRoles(roles);
        return this.save(user);
    }

    @Override
    public void sendRequestEmailAgain(String email) {
        logger.info("############## Authentication Service /api/auth/request-token-email started");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User account",
                                HttpStatus.NOT_FOUND,
                                List.of(
                                        new ErrorDetail("email", "Cannot find your email or account system")
                                ),
                                "/system/user"
                        )
                );
        TokenStore tokenStore = tokenStoreRepository.findTopByUserAndTokenTypeOrderByCreatedDateTimeDesc(user, TokenType.EMAIL_VERIFYCATION)
                .orElseThrow(() ->
                        new EmailVerifycationException(
                                "Verify email issue",
                                HttpStatus.BAD_REQUEST,
                                List.of(
                                        new ErrorDetail("email", "No verification token found")
                                ),
                                "/system/verify"
                        )
                );
        if (!tokenStore.isExpired() && !tokenStore.canRequestAgain(5)) {
            throw new EmailVerifycationException(
                    "Too many requests",
                    HttpStatus.TOO_MANY_REQUESTS,
                    List.of(new ErrorDetail("email", "Please wait before requesting a new email verification.")),
                    "/system/verify"
            );
        }

        String verificationToken = tokenProvider.generateTokenWithEmail(user.getEmail());

        TokenStore tokenStore1 = saveToken(user, verificationToken, TokenType.EMAIL_VERIFYCATION);
        tokenStoreRepository.save(tokenStore1);

        emailImplementService.sendMailRegister(user.getEmail(),verificationToken);
        logger.info("############## Authentication Service /api/auth/request-token-email finished");
    }

    @Override
    public void forgotPassword(String email) {
        logger.info("############## Authentication Service /api/auth/forgot-password started");
        User user = findUserWithEmail(email);
        Optional<TokenStore> tokenStoreOpt = tokenStoreRepository.findTopByUserAndTokenTypeOrderByCreatedDateTimeDesc(user, TokenType.FORGOT_PASSWORD);

        if(tokenStoreOpt.isPresent()) {
            TokenStore tokenStore = tokenStoreOpt.get();
            if (!tokenStore.isExpired() && !tokenStore.canRequestAgain(5)) {
                throw new EmailVerifycationException(
                        "Too many requests",
                        HttpStatus.TOO_MANY_REQUESTS,
                        List.of(new ErrorDetail("email", "Please wait before requesting a new email verification.")),
                        "/system/forgot-password"
                );
            }
        }

        String verificationToken = tokenProvider.generateTokenWithEmail(user.getEmail());

        TokenStore tokenStore1 = saveToken(user, verificationToken, TokenType.FORGOT_PASSWORD   );
        tokenStoreRepository.save(tokenStore1);

        emailImplementService.sendMailRetrievePassword(user.getEmail(),verificationToken);
        logger.info("############## Authentication Service /api/auth/forgot-password finished");
    }

    @Override
    public void resetPassword(ResetPasswordRequest request, BindingResult result) {
        logger.info("############## Authentication Service /api/auth/is-new-password started");
        if (result.hasErrors()) {
            logger.info("------------ validation error: " + result.getAllErrors().size());

            List<ErrorDetail> errors = result.getFieldErrors().stream()
                    .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                    .toList();

            throw new ResetPasswordException(
                    "Missing property",
                    HttpStatus.BAD_REQUEST,
                    errors,
                    "/system/reset-password"
            );
        }
        TokenStore tokenStore = tokenStoreRepository.findByTokenAndTokenTypeAndRevokedFalseAndExpiresAtAfter(
                request.getToken(), TokenType.FORGOT_PASSWORD, LocalDateTime.now()
        ).orElseThrow(() ->
                new EmailVerifycationException(
                        "Reset password issue",
                        HttpStatus.BAD_REQUEST,
                        List.of(
                                new ErrorDetail("email", "Your token is expired or cannot find your token!!!")
                        ),
                        "/system/reset-password"
                )
        );
        User user = tokenStore.getUser();
        if (user == null) {
            throw new UserNotFoundException(
                    "User not found",
                    HttpStatus.NOT_FOUND,
                    List.of(new ErrorDetail("user", "User associated with this token does not exist")),
                    "/system/reset-password"
            );
        }
        String password = request.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        tokenStore.setRevoked(true);
        tokenStoreRepository.save(tokenStore);
        logger.info("############## Authentication Service /api/auth/is-new-password finished");
    }
}
