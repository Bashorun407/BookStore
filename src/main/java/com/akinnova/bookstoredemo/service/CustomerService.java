package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.CustomerDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.dto.UpdateCustomerDto;
import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.entity.Roles;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Roles roles;

    // TODO: 6/27/2023 (Create, Login, update)
    //1) Method to create customer
    public ResponseEntity<?> createCustomer(CustomerDto customerDto){
        //check if Customer exists
        if(customerRepository.existsByUsernameOrEmail(customerDto.getUsername(), customerDto.getEmail())){
            throw new ApiException("User with the username or email already exists");
        }

        Customer customer = Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .dateOfBirth(customerDto.getDateOfBirth())
                .username(customerDto.getUsername())
                .email(customerDto.getEmail())
                .password(customerDto.getPassword())
                .build();

        // TODO: 6/27/2023 (Here, a mail should be sent to user for confirmation)

        return new ResponseEntity<>("Customer create successfully", HttpStatus.CREATED);
    }

    //2) Method to login customer
    public ResponseEntity<?> login(LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
    }

    //3) Method to Update Customer password....email
    public ResponseEntity<?> updateCustomerPassword(UpdateCustomerDto updateCustomerDto) {
        //Verify user detail
        Optional<Customer> customerOptional = customerRepository.findByEmail(updateCustomerDto.getEmail());
        customerOptional.orElseThrow(()-> new ApiException("Customer with the email not found"));

        //If customer is not available, throw an exception
        Customer customer = Customer.builder()
                .username(updateCustomerDto.getUsername())
                .email(updateCustomerDto.getEmail())
                .password(updateCustomerDto.getNewPassword())
                .build();
        return new ResponseEntity<>("Update done successfully", HttpStatus.OK);

    }
}
