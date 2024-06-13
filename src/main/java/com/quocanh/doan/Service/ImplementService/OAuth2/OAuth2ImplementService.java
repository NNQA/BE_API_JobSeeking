package com.quocanh.doan.Service.ImplementService.OAuth2;

import com.quocanh.doan.Exception.OAuth2AuthenticationProcessingException;
import com.quocanh.doan.Exception.Signup.SignupException;
import com.quocanh.doan.Model.AuthProvider;
import com.quocanh.doan.Model.ERole;
import com.quocanh.doan.Model.Role;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.RoleRepository;
import com.quocanh.doan.Repository.UserRepository;
import com.quocanh.doan.Security.Oauth2.OAuth2UserInfo;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.Interface.OAuth2.Oauth2ServiceLayer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OAuth2ImplementService implements Oauth2ServiceLayer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public  OAuth2ImplementService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public User storageUser(OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest) {
        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signup with" + user.getProvider() + " account. Please use your " + user.getProvider() +
                                " account to login."
                );
            }
            user = updateExistingUser(user, oAuth2UserInfo);

        } else {
            user = this.registerUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return user;
    }
    @Override
    public User registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {

        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setCheckCode(true);
        Set<Role> roles = new HashSet<>();
        Role supplierRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new SignupException("Error: Role is not found."));
        roles.add(supplierRole);
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }


}
