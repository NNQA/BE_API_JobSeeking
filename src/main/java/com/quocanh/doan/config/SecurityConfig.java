    package com.quocanh.doan.config;


    import com.quocanh.doan.config.CustomAuthenticationProvider.ExpandDaoAuthenticationProvider;
    import com.quocanh.doan.config.Jwt.AuthTokenFilter;
    import com.quocanh.doan.config.Oauth2.HttpCookieOauth2AuthorizationRequestRepository;
    import com.quocanh.doan.config.Oauth2.ImplementOauth2UserService;
    import com.quocanh.doan.config.Oauth2.OAuth2AuthenticationSuccessHandler;
    import com.quocanh.doan.config.Oauth2.RestAuthEntryPoint;
    import com.quocanh.doan.config.Oauth2.OAuth2AuthenticationFailureHandler;
    import com.quocanh.doan.Service.ImplementService.User.UserDetailsImplementService;
    import lombok.AllArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(
            securedEnabled = true,
            jsr250Enabled = true,
            prePostEnabled = true
    )
    @AllArgsConstructor
    public class SecurityConfig {

//        private final ImplementOauth2UserService implementOauth2UserService;
//        private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
        private final UserDetailsImplementService userDetailsImplementService;
//        private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
        private final RestAuthEntryPoint restAuthEntryPoint;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
            return authConfig.getAuthenticationManager();
        }
        @Bean
        public ExpandDaoAuthenticationProvider authenticationProvider() {
            ExpandDaoAuthenticationProvider authenticationProvider = new ExpandDaoAuthenticationProvider();

            authenticationProvider.setUserDetailsService(userDetailsImplementService);
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }
        @Bean
        public AuthTokenFilter tokenAuthenticationFilter() {
            return new AuthTokenFilter();
        }
        @Bean
        public HttpCookieOauth2AuthorizationRequestRepository cookieOauth2AuthorizationRequestRepository() {
            return new HttpCookieOauth2AuthorizationRequestRepository();
        }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exception -> exception.authenticationEntryPoint(restAuthEntryPoint))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(
                            authorize -> authorize
                                    .requestMatchers("/api/auth/**","/api/clientController/**", "/oauth2/**").permitAll()
                                    .anyRequest().authenticated()
                    )
//                    .oauth2Login(
//                            oauth -> oauth
//                                    .authorizationEndpoint(authEnpoint -> authEnpoint
//                                            .baseUri("/oauth2/authorize")
//                                            .authorizationRequestRepository(cookieOauth2AuthorizationRequestRepository()))
//                                    .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
//                                            .baseUri("/oauth2/callback/*"))
//                                    .userInfoEndpoint(
//                                            userInfoEndpointConfig -> userInfoEndpointConfig.userService(implementOauth2UserService)
//                                    )
//                                    .successHandler(oAuth2AuthenticationSuccessHandler)
//                                    .failureHandler(oAuth2AuthenticationFailureHandler)
                    .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            http.authenticationProvider(authenticationProvider());
            return http.build();
        }

    }
