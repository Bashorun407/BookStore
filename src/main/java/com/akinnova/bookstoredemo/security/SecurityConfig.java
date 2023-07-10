package com.akinnova.bookstoredemo.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    //1) Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize->
                        authorize.regexMatchers(HttpMethod.POST, "/api/v1/bookstore/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/bookstore/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.PUT, "/api/v1/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.DELETE, "/api/v1/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/testing/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/customer/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/customer/auth/login").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/customer/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.PUT, "/api/v1/customer/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/comment/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/comment/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.DELETE, "/api/v1/comment/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/review/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/review/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/likes/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/likes/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/rates/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/rates/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/cart/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/cart/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.PUT, "/api/v1/cart/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.DELETE, "/api/v1/cart/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/transaction/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.GET, "/api/v1/transaction/auth/(.*)").permitAll()
                                .regexMatchers(HttpMethod.POST, "/api/v1/email/auth/(.*)").permitAll()
                                .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }


//  The following method was the original implementation
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize->
//                        authorize.requestMatchers(HttpMethod.POST, "/api/v1/bookstore/auth/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/bookstore/auth/**").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/**").permitAll()
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/auth/**").permitAll()
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/auth/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/testing/**").permitAll()
//                                .anyRequest().authenticated()
//                ).httpBasic(Customizer.withDefaults());
//
//        return httpSecurity.build();
//    }

    //3)Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

