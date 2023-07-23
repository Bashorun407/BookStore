package com.akinnova.bookstoredemo.service.customerservice;

import com.akinnova.bookstoredemo.dto.customerdto.CustomerDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.dto.customerdto.CustomerUpdateDto;
import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface ICustomerService {
    ResponsePojo<Customer> createCustomer(CustomerDto customerDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponseEntity<?> findCustomerByUsername(String username);
    ResponseEntity<?> findCustomerByEmail(String email);
    ResponseEntity<?> findAllCustomers();
    ResponsePojo<Customer> updateCustomer(CustomerUpdateDto updateCustomerDto);
    ResponseEntity<?> searchCustomer(String firstName, String lastName, String username, String email, Pageable pageable);
}
