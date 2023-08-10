package com.akinnova.bookstoredemo.security;

import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
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
public class CustomCustomerUserDetialsService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Customer with this username not found" + username));

        //I do not know if subsequent code will work
        Set<GrantedAuthority> authorities = customer.getRoles().stream()
                .map((role)-> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

        return new User(customer.getUsername(), customer.getPassword(), authorities);
    }

}
