    package com.quocanh.doan.Security;


    import com.quocanh.doan.Security.Oauth2.HttpCookieOauth2AuthorizationRequestRepository;
    import com.quocanh.doan.Security.Oauth2.ImplementOauth2UserService;
    import com.quocanh.doan.Security.Oauth2.OAuth2AuthenticationSuccessHandler;
    import com.quocanh.doan.Security.Oauth2.RestAuthEntryPoint;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(
            securedEnabled = true,
            jsr250Enabled = true,
            prePostEnabled = true
    )
    public class SecurityConfig {

        private final ImplementOauth2UserService implementOauth2UserService;
        private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

        public SecurityConfig(ImplementOauth2UserService implementOauth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
            this.implementOauth2UserService = implementOauth2UserService;
            this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        }
        @Bean
        public HttpCookieOauth2AuthorizationRequestRepository cookieOauth2AuthorizationRequestRepository() {
            return new HttpCookieOauth2AuthorizationRequestRepository();
        }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exception -> exception.authenticationEntryPoint(new RestAuthEntryPoint()))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(
                            authorize -> authorize.requestMatchers("/api/auth/**", "/oauth2/**").permitAll()
                    )
                    .oauth2Login(
                            oauth -> oauth
                                    .authorizationEndpoint(authEnpoint -> authEnpoint
                                            .baseUri("/oauth2/authorize")
                                            .authorizationRequestRepository(cookieOauth2AuthorizationRequestRepository()))
                                    .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
                                            .baseUri("/oauth2/callback/*"))
                                    .userInfoEndpoint(
                                            userInfoEndpointConfig -> userInfoEndpointConfig.userService(implementOauth2UserService)
                                    )
                                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    )
            ;
            return http.build();
        }

    }
