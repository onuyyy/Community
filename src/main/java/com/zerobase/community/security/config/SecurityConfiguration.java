package com.zerobase.community.security.config;

import com.zerobase.community.security.UserAuthenticationFailureHandler;
import com.zerobase.community.security.filter.JwtAuthenticationfilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationfilter jwtAuthenticationfilter;

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
                // jwtAuthenticationfilter 필터가 먼저 실행된다
        ).addFilterBefore(jwtAuthenticationfilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/favicon.ico", "/files/**")
                .permitAll()
                .requestMatchers("/", "/signup/**", "/signin/**")
                .permitAll()
                .requestMatchers("/board/**").hasAnyRole("USER_WRITE", "USER_UPDATE")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
        ).formLogin(form -> form
                //.loginPage("/signin")
                //.defaultSuccessUrl("/",true) // 로그인 성공 시
                // .failureUrl("/fail") // 로그인 실패 시
                .failureHandler(authenticationFailureHandler())
                .permitAll()
        ).exceptionHandling(except ->
                except.accessDeniedPage("/error/denied"));

        return http.build();
    }
}