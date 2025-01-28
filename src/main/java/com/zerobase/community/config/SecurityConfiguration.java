package com.zerobase.community.config;

import com.zerobase.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // login 실패시 호출
    @Bean
    public UserAuthenticationFailureHandler authenticationFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }

    // user 정보 넘겨주는 부분
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/favicon.ico", "/files/**").permitAll()
                .requestMatchers("/", "/signup/**", "/logout")
                .permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
        ).exceptionHandling(except -> except
                .accessDeniedPage("/error/denied"));

        return http.build();
    }
}