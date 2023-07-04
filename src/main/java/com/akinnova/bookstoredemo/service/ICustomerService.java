package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.dto.CustomerDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.dto.UpdateCustomerDto;
import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ICustomerService {
    ResponsePojo<Customer> createCustomer(CustomerDto customerDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponsePojo<Customer> findCustomerByUsername(String username);
    ResponsePojo<Customer> findCustomerByEmail(String email);
    ResponsePojo<List<Customer>> findAllCustomers();
    ResponsePojo<Customer> updateCustomerPassword(UpdateCustomerDto updateCustomerDto);
}
