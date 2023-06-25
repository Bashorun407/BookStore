package com.akinnova.bookstoredemo.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    //1) Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //2)Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize->
                        authorize.requestMatchers(HttpMethod.POST, "/api/v1/bookstore/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/bookstore/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/testing/**").permitAll()
                                .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    //3)Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
