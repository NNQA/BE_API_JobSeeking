package com.quocanh.doan.Service.ImplementService.User;

import com.quocanh.doan.Exception.CheckCode.EmailVerifycationException;
import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.UserNotFoundException;
import com.quocanh.doan.Model.Enum.TokenType;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.TokenStoreRepository;
import com.quocanh.doan.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsImplementService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenStoreRepository tokenRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOremail) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailOrUserName(usernameOremail, usernameOremail)
                .orElseThrow(() -> new UserNotFoundException("User not found with " + usernameOremail));

        if (isEmailVerified(user)) {
            throw new EmailVerifycationException(
                    "Verify email issue",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("email", "Your token is expired or cannot find your token!!!")
                    ),
                    "/system/verify"
            );
        }
        return UserPrincipal.build(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with " + id)
        );
        if (!user.isVerifiedEmail()) {
            throw new EmailVerifycationException(
                    "Verify email issue",
                    HttpStatus.BAD_REQUEST,
                    List.of(
                            new ErrorDetail("email", "Your token is expired or cannot find your token!!!")
                    ),
                    "/system/verify"
            );
        }
        return UserPrincipal.build(user);
    }

    private boolean isEmailVerified(User user) {
        return !tokenRepository.existsByUserIdAndTokenTypeAndRevokedFalse(user.getId(), TokenType.EMAIL_VERIFYCATION);
    }
}

