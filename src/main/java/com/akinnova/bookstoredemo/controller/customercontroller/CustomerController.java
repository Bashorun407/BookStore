package com.akinnova.bookstoredemo.controller.customercontroller;

import com.akinnova.bookstoredemo.dto.customerdto.CustomerDto;
import com.akinnova.bookstoredemo.dto.customerdto.CustomerResponseDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.dto.customerdto.CustomerUpdateDto;
import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.customerservice.CustomerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer/auth")
@AllArgsConstructor
@NoArgsConstructor

public class CustomerController {
    @Autowired
    private CustomerServiceImpl customerService;

    //1) Method to create customer
    @PostMapping("/customer")
    public ResponsePojo<CustomerResponseDto> createCustomer(@RequestBody CustomerDto customerDto){
        return customerService.createCustomer(customerDto);
    }

    //2) Method to login customer
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return customerService.login(loginDto);
    }

    //3) Method to find customer by username
    @GetMapping("/customer/{username}")
    public ResponseEntity<?> findCustomerByUsername(@PathVariable(name = "username") String username) {
        return customerService.findCustomerByUsername(username);
    }

    //4) Method to find customer by email
    @GetMapping("/customerEmail/{email}")
    public ResponseEntity<?> findCustomerByEmail(@PathVariable(name = "email") String email) {
        return customerService.findCustomerByEmail(email);
    }

    //5) Method to find all customers
    @GetMapping("/customers")
    public ResponseEntity<?> findAllCustomers(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        return customerService.findAllCustomers(pageNum, pageSize);
    }

    //6) Method to Update Customer password....email
    @PutMapping("/customer")
    public ResponsePojo<CustomerResponseDto> updateCustomer(@RequestBody CustomerUpdateDto updateCustomerDto) {
        return customerService.updateCustomer(updateCustomerDto);
    }

    //7) Method to search customer's details using multiple parameters
    @GetMapping("/search")
    public ResponseEntity<?> searchCustomer(@RequestParam(name = "firstName", required = false) String firstName,
                                                       @RequestParam(name = "lastName", required = false) String lastName,
                                                       @RequestParam(name = "username", required = false) String username,
                                                       @RequestParam(name = "email", required = false) String email,
                                                       Pageable pageable) {
        return customerService.searchCustomer(firstName, lastName, username, email, pageable);
    }
}
