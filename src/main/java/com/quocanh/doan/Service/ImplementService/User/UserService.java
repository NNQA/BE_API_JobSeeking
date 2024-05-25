package com.quocanh.doan.Service.ImplementService.User;

import com.quocanh.doan.Exception.Signup.EmailExeption;
import com.quocanh.doan.Exception.Signup.EmailExistException;
import com.quocanh.doan.Exception.Signup.PasswordException;
import com.quocanh.doan.Exception.Signup.PasswordLengthException;
import com.quocanh.doan.Model.AuthProvider;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.UserRepository;
import com.quocanh.doan.Service.Interface.UserIntef.IUserService;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
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
//        User user = new User();
//        user.setEmail(signupRequest.getEmail());
//        user.setPassword(encoder.encode(signupRequest.getPassword()));
//        user.setProvider(AuthProvider.local);
//
//        this.save(user);
    }
}
