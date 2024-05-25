package com.quocanh.doan.Service.ImplementService.User;

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
                .orElseThrow(() -> new UsernameNotFoundException("User not found with " + usernameOremail));

        return UserPrincipal.build(user);
    }
}
