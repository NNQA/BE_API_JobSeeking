package com.quocanh.doan.Service.ImplementService.User;

import com.quocanh.doan.Exception.CheckCode.CheckCodeException;
import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Exception.Signup.*;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Model.AuthProvider;
import com.quocanh.doan.Model.ERole;
import com.quocanh.doan.Model.Role;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.RoleRepository;
import com.quocanh.doan.Repository.UserRepository;
import com.quocanh.doan.Service.ImplementService.Email.EmailImplementService;
import com.quocanh.doan.Service.Interface.UserIntef.IUserService;
import com.quocanh.doan.Utils.AppConstants;
import com.quocanh.doan.config.Jwt.TokenProvider;
import com.quocanh.doan.dto.request.UserUpdate;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final EmailImplementService emailImplementService;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder encoder, EmailImplementService emailImplementService,
                       TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.emailImplementService = emailImplementService;
        this.roleRepository = roleRepository;
        this.tokenProvider = tokenProvider;
    }
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
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword()))
                .provider(AuthProvider.local)
                .verifyEmail(verificationToken)
                .verified(false)
                .roles(Set.of(
                        roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new SignupException("Error: Role is not found."))
                ))
                .isNewUser(true)
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .build();

        this.save(user);
        emailImplementService.sendMailRegister(signupRequest.getEmail(),
                AppConstants.REDIRECT_URL_FE + "/auth/verify?token=" + verificationToken
        );
    }
    @Override
    public void VerifiedCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Cannot found user in system!!!"));

//        if(user().equals(code)) {
//            user.setCheckCode(true);
//            this.save(user);
//        } else {
//            throw new CheckCodeException("Your code provided does not match");
//        }
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
}
