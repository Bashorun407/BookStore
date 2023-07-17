package com.akinnova.bookstoredemo.service.customerservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.customerdto.CustomerDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.dto.customerdto.UpdateCustomerDto;
import com.akinnova.bookstoredemo.email.emailDto.EmailDetail;
import com.akinnova.bookstoredemo.email.emailService.EmailService;
import com.akinnova.bookstoredemo.entity.Customer;
import com.akinnova.bookstoredemo.entity.QCustomer;
import com.akinnova.bookstoredemo.entity.Roles;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.repository.RolesRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private EntityManager entityManager;
    private final CustomerRepository customerRepository;
    private final RolesRepository rolesRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Class Constructor
    public CustomerServiceImpl(CustomerRepository customerRepository, RolesRepository rolesRepository,
                               AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                               EmailService emailService) {
        this.customerRepository = customerRepository;
        this.rolesRepository = rolesRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

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
                        + "\n\nYour account has been successfully created.".indent(4)  +
                        "\n\nTo continue, clink the link attached: "
                        + "\n(www.link.com)".indent(5))
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

    //7) Method to search customer's details using multiple parameters
    @Override
    public ResponsePojo<Page<Customer>> searchCustomer(String firstName, String lastName, String username,
                                                       String email, Pageable pageable) {

        QCustomer qCustomer = QCustomer.customer;
        BooleanBuilder predicate = new BooleanBuilder();

        if(StringUtils.hasText(firstName))
            predicate.and(qCustomer.firstName.likeIgnoreCase("%" + firstName + "%"));

        if(StringUtils.hasText(lastName))
            predicate.and(qCustomer.lastName.likeIgnoreCase("%" + lastName + "%"));

        if(StringUtils.hasText(username))
            predicate.and(qCustomer.username.likeIgnoreCase("%" + username + "%"));

        if(StringUtils.hasText(email))
            predicate.and(qCustomer.email.likeIgnoreCase("%" + email + "%"));

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<Customer> jpaQuery = jpaQueryFactory.selectFrom(qCustomer)
                .where(predicate)
                .orderBy(qCustomer.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //Pageable customer response
        Page<Customer> customerPage = new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());

        //Response POJO
        ResponsePojo<Page<Customer>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("Customers: ");
        responsePojo.setData(customerPage);

        return responsePojo;
    }

}
