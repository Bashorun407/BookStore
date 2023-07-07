package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.CustomerDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.dto.UpdateCustomerDto;
import com.akinnova.bookstoredemo.email.emailDto.EmailDetail;
import com.akinnova.bookstoredemo.email.emailService.EmailService;
import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.entity.Roles;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.repository.RolesRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
//    @Autowired
//    private Roles roles;

    // TODO: 6/27/2023 (Create, Login, update)
    //1) Method to create customer
    public ResponsePojo<Customer> createCustomer(CustomerDto customerDto){
        //check if Customer exists
        if(customerRepository.existsByUsername(customerDto.getUsername())){
            throw new ApiException("User with the username or email already exists");
        }

        Customer customer = Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .dateOfBirth(customerDto.getDateOfBirth())
                .username(customerDto.getUsername())
                .email(customerDto.getEmail())
                .password(passwordEncoder.encode(customerDto.getPassword()))
                .build();

        //To save data to customer repository
       Customer savedCustomer =  customerRepository.save(customer);

        //To save roles in the roles database
        Roles roles = Roles.builder()
                .roleName(customerDto.getRole())
                .build();
        rolesRepository.save(roles);

       //Body of email to send to user's mail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(customerDto.getEmail())
                .subject("BookReader Account Creation")
                .body("Congratulations " + savedCustomer.getFirstName() +", " + savedCustomer.getLastName()
                        + "!!\n Your account has been successfully created."  +
                        "\n To continue, clink the link attached: " + "(attached link)")
                .build();

        //Sending mail to user
        emailService.sendSimpleEmail(emailDetail);

        // TODO: 6/27/2023 (Here, a mail should be sent to user for confirmation)
        ResponsePojo<Customer> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.CREATED);
        responsePojo.setMessage(String.format(ResponseUtils.CREATED_MESSAGE, customerDto.getUsername()));
        responsePojo.setData(customer);
        return responsePojo;
    }

    //2) Method to login customer
    public ResponseEntity<?> login(LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
    }

    //3) Method to find customer by username
    @Override
    public ResponsePojo<Customer> findCustomerByUsername(String username) {
        Optional<Customer> customerOptional = customerRepository.findByUsername(username);
        customerOptional.orElseThrow(()-> new ApiException("Customer with specified username does not exist"));

        ResponsePojo<Customer> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format(ResponseUtils.FOUND_MESSAGE, username));
        responsePojo.setData(customerOptional.get());
        return responsePojo;
    }

    //4) Method to find customer by email
    @Override
    public ResponsePojo<Customer> findCustomerByEmail(String email) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        customerOptional.orElseThrow(()-> new ApiException("Customer with specified email does not exist"));

        ResponsePojo<Customer> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format(ResponseUtils.FOUND_MESSAGE, email));
        responsePojo.setData(customerOptional.get());
        return responsePojo;
    }

    //5) Method to find all customers
    @Override
    public ResponsePojo<List<Customer>> findAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();

        ResponsePojo<List<Customer>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("Customer list:");
        responsePojo.setData(customerList);
        return responsePojo;
    }

    //6) Method to Update Customer password....email
    public ResponsePojo<Customer> updateCustomerPassword(UpdateCustomerDto updateCustomerDto) {
        //Verify user detail
        Optional<Customer> customerOptional = customerRepository.findByEmail(updateCustomerDto.getEmail());
        //If customer is not available, throw an exception
        customerOptional.orElseThrow(()-> new ApiException("Customer with the email not found"));

        //A temporary container to hold fetched customer
       Customer customerToUpdate = customerOptional.get();

       //Updating fetched customer
         customerToUpdate.setUsername(updateCustomerDto.getUsername());
         customerToUpdate.setEmail(updateCustomerDto.getEmail());
         customerToUpdate.setPassword(passwordEncoder.encode(updateCustomerDto.getNewPassword()));

        //Saving updated customer details to customer repository
       Customer savedCustomer = customerRepository.save(customerToUpdate);

        //Body of email to send to user's mail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(savedCustomer.getEmail())
                .subject("Updated BookReader Account Details")
                .body("Hello " + savedCustomer.getFirstName() + ", " + savedCustomer.getLastName()
                        + "!!\n Your account has been successfully updated.")
                .build();

        //Sending mail to user
        emailService.sendSimpleEmail(emailDetail);

        ResponsePojo<Customer> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.OK);
        responsePojo.setMessage(String.format("Customer with email %s has been updated", updateCustomerDto.getEmail()));
        responsePojo.setData(customerToUpdate);
        return responsePojo;
    }
}
