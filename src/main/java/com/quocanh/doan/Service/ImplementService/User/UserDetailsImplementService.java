package com.quocanh.doan.Service.ImplementService.User;

import com.quocanh.doan.Exception.CheckCode.CheckCodeException;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImplementService implements UserDetailsService {

    private final UserRepository userRepository;
    public UserDetailsImplementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOremail) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmailOrName(usernameOremail, usernameOremail)
                .orElseThrow(() -> new UserNotFoundException("User not found with " + usernameOremail));
        if(!user.isCheckCode()) {
            throw new CheckCodeException("User not verified. Please check your mail for the verification code");
        }
        return UserPrincipal.build(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with " + id)
        );
        if(!user.isCheckCode()) {
            throw new CheckCodeException("User not verified. Please check your mail for the verification code");
        }
        return UserPrincipal.build(user);
    }

}
