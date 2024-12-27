package com.quocanh.doan.Service.ImplementService.User;

import com.quocanh.doan.Exception.CheckCode.CheckCodeException;
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
import com.quocanh.doan.dto.request.UserUpdate;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;
    private final EmailImplementService emailImplementService;

    private final RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder encoder, EmailImplementService emailImplementService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.emailImplementService = emailImplementService;
        this.roleRepository = roleRepository;
    }
    @Override
    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public void signUp(SignupRequest signupRequest) {

        if(signupRequest.getEmail().isEmpty()) {
            throw new EmailExeption("Do not leave email blank!!!");
        }
        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailExistException("Your email is exist my system!!!");
        }
        if(signupRequest.getPassword().isEmpty()) {
            throw new PasswordException("Do not leave password blank");
        }
        if (signupRequest.getPassword().length() < 4) {
            throw new PasswordLengthException("Password needs to be at least 4 characters");
        }
        Random  random = new Random();

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setProvider(AuthProvider.local);

        user.setCheckCode(false);
        Set<Role> roles = new HashSet<>();
        Role supplierRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new SignupException("Error: Role is not found."));
        roles.add(supplierRole);
        user.setRoles(roles);
        int code = random.nextInt(500);
        user.setCodeConfirm(String.valueOf(code));
        user.setNewUser(true);

        emailImplementService.sendMailRegister(signupRequest.getEmail(), String.valueOf(code));
        this.save(user);
    }
    @Override
    public void VerifiedCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Cannot found user in system!!!"));

        if(user.getCodeConfirm().equals(code)) {
            user.setCheckCode(true);
            this.save(user);
        } else {
            throw new CheckCodeException("Your code provided does not match");
        }
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
            user.setName(userUpdate.getName());
             this.userRepository.save(user);
        } catch (DataAccessException ex) {
            logger.error(ex.getMessage());
            throw new UserNotFoundException(ex.getMessage());
        }
    }
}
