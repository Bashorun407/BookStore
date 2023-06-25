package com.akinnova.bookstoredemo.security;

import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AdminEntity admin = adminRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(()-> new UsernameNotFoundException("User with username: not found " + username));

        Set<GrantedAuthority> authorities = admin.getRolesSet().stream()
                .map((role)-> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}
