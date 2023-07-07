package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.dto.CustomerDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.dto.UpdateCustomerDto;
import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.CustomerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer/auth")
@AllArgsConstructor
@NoArgsConstructor

public class CustomerController {
    @Autowired
    private CustomerServiceImpl customerService;

    //1) Method to create customer
    @PostMapping("/customer")
    public ResponsePojo<Customer> createCustomer(@RequestBody CustomerDto customerDto){
        return customerService.createCustomer(customerDto);
    }

    //2) Method to login customer
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return customerService.login(loginDto);
    }

    //3) Method to find customer by username
    @GetMapping("/customer/{username}")
    public ResponsePojo<Customer> findCustomerByUsername(@PathVariable(name = "username") String username) {
        return customerService.findCustomerByUsername(username);
    }

    //4) Method to find customer by email
    @GetMapping("/customerEmail/{email}")
    public ResponsePojo<Customer> findCustomerByEmail(@PathVariable(name = "email") String email) {
        return customerService.findCustomerByEmail(email);
    }

    //5) Method to find all customers
    @GetMapping("/customers")
    public ResponsePojo<List<Customer>> findAllCustomers() {
        return customerService.findAllCustomers();
    }

    //6) Method to Update Customer password....email
    @PutMapping("/customer")
    public ResponsePojo<Customer> updateCustomerPassword(@RequestBody UpdateCustomerDto updateCustomerDto) {
        return customerService.updateCustomerPassword(updateCustomerDto);
    }
}
