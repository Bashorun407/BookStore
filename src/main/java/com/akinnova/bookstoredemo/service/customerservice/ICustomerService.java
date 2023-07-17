package com.akinnova.bookstoredemo.service.customerservice;

import com.akinnova.bookstoredemo.dto.customerdto.CustomerDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.dto.customerdto.UpdateCustomerDto;
import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ICustomerService {
    ResponsePojo<Customer> createCustomer(CustomerDto customerDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponsePojo<Customer> findCustomerByUsername(String username);
    ResponsePojo<Customer> findCustomerByEmail(String email);
    ResponsePojo<List<Customer>> findAllCustomers();
    ResponsePojo<Customer> updateCustomerPassword(UpdateCustomerDto updateCustomerDto);
    ResponsePojo<Page<Customer>> searchCustomer(String firstName, String lastName, String username, String email, Pageable pageable);
}
