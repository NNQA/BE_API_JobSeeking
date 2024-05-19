package com.quocanh.doan.Service.ImplementService.OAuth2;

import com.quocanh.doan.Exception.OAuth2AuthenticationProcessingException;
import com.quocanh.doan.Model.AuthProvider;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Repository.UserRepository;
import com.quocanh.doan.Security.Oauth2.OAuth2UserInfo;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.Service.Interface.OAuth2.Oauth2ServiceLayer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuth2ImplementService implements Oauth2ServiceLayer {

    private final UserRepository userRepository;
    public  OAuth2ImplementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User storageUser(OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest) {
        System.out.println("storageUser");
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

        userRepository.save(user);
        return user;
    }

    @Override
    public User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        return null;
    }


}
