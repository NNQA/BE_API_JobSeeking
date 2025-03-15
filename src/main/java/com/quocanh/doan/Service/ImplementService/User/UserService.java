package com.quocanh.doan.Service.ImplementService.User;

import com.quocanh.doan.Exception.CheckCode.EmailVerifycationException;
import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.Signup.*;
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
import com.quocanh.doan.Service.Interface.UserIntef.IUserService;
import com.quocanh.doan.Utils.AppConstants;
import com.quocanh.doan.config.Jwt.TokenProvider;
import com.quocanh.doan.dto.request.UserUpdate;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final EmailImplementService emailImplementService;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private final TokenStoreRepository tokenStoreRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public User save(User user) {
        userRepository.save(user);
        return user;
    }
    private List<Map<String, String>> createMessageError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return Map.of(
                                "name", fieldError.getField(),
                                "message", Objects.requireNonNull(fieldError.getDefaultMessage())
                        );
                    } else {
                        return Map.of(
                                "name", "general",
                                "message", Objects.requireNonNull(error.getDefaultMessage())
                        );
                    }
                })
                .toList();
    }
    private User saveUser(SignupRequest signupRequest) {
        return User.builder()
                .email(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword()))
                .provider(AuthProvider.local)
                .verifiedEmail(false)
                .roles(Set.of(
                        roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new SignupException("Error: Role is not found."))
                ))
                .isNewUser(true)
                .build();
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
        if (result.hasErrors()) {
            logger.info("------------ validation error: " + result.getAllErrors().size());

            List<ErrorDetail> errors = result.getFieldErrors().stream()
                    .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                    .toList();

            throw new SignupException(
                    "Missing property",
                    HttpStatus.BAD_REQUEST,
                    errors,
                    "/system/signup"
            );
        }
        if(signupRequest.getEmail().isEmpty()) {
            throw new EmailExeption(
                    "Email issue",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("email", "Do not leave email blank!!!")
                    ),
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
        if(signupRequest.getPassword().isEmpty()) {
            throw new SignupException(
                    "password issue",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("password", "Do not leave password blank!!!")
                    ),
                    "/system/signup"
            );
        }
        if(signupRequest.getPassword().length() < 4) {
            throw new SignupException(
                    "password issue",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("password", "Password needs to be at least 4 characters!!!")
                    ),
                    "/system/signup"
            );
        }
        logger.info("################ finished validation");
        String verificationToken = tokenProvider.generateTokenWithEmail(signupRequest.getEmail());

        User user = saveUser(signupRequest);
        TokenStore tokenStore = saveToken(user, verificationToken, TokenType.EMAIL_VERIFYCATION);
        this.save(user);
        tokenStoreRepository.save(tokenStore);
        emailImplementService.sendMailRegister(signupRequest.getEmail(), verificationToken);
    }

    @Override
    public void verifyEmailWithToken(String token) {
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

        user.setVerifiedEmail(true);
        this.save(user);
    }
    @Override
    public User updateRoleUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with " + id)
        );
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new SignupException("Error: Role is not found."));
        Role supplierRole = roleRepository.findByName(ERole.ROLE_SUPPLIER)
                .orElseThrow(() -> new SignupException("Error: Role is not found."));
        roles.add(supplierRole);
        roles.add(userRole);
        user.setRoles(roles);
        return this.save(user);
    }

    @Override
    public void updateNewUser(UserPrincipal userPrincipal,UserUpdate userUpdate) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(
                () -> new UserNotFoundException("User not found with " + userPrincipal.getId())
        );

        try {
            user.setUniversity(userUpdate.getUniversity());
            user.setExperiencelevel(userUpdate.getExperiencelevel());
            user.setNewUser(false);
            user.setPhone(userUpdate.getPhone());
             this.userRepository.save(user);
        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            throw new UserNotFoundException(ex.getMessage());
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void sendRequestEmailAgain(String email) {
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
    }
}
