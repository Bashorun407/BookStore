package com.akinnova.bookstoredemo.service.customerservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.customerdto.CustomerDto;
import com.akinnova.bookstoredemo.dto.customerdto.CustomerResponseDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.dto.customerdto.CustomerUpdateDto;
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
import lombok.AllArgsConstructor;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponsePojo<CustomerResponseDto> createCustomer(CustomerDto customerDto){
        //check if Customer exists
        if(customerRepository.existsByUsername(customerDto.getUsername())){
            throw new ApiException("User with the username or email already exists");
        }

        Customer customer = Customer.builder()
                .imageAddress(customerDto.getImageAddress())
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .dateOfBirth(customerDto.getDateOfBirth())
                .phoneNumber(customerDto.getPhoneNumber())
                .username(customerDto.getUsername())
                .email(customerDto.getEmail())
                .password(passwordEncoder.encode(customerDto.getPassword()))
                .build();

        //To save data to customer repository
       Customer savedCustomer =  customerRepository.save(customer);
       CustomerResponseDto responseDto = CustomerResponseDto.builder()
               .imageAddress(savedCustomer.getImageAddress())
               .firstName(savedCustomer.getFirstName())
               .lastName(savedCustomer.getLastName())
               .build();

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
        ResponsePojo<CustomerResponseDto> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.CREATED);
        responsePojo.setMessage(String.format(ResponseUtils.CREATED_MESSAGE, customerDto.getUsername()));
        responsePojo.setData(responseDto);
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
    public ResponseEntity<?> findCustomerByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()-> new ApiException(String.format("Customer with username: %s does not exist", username)));
        CustomerResponseDto responseDto = CustomerResponseDto.builder()
                .imageAddress(customer.getImageAddress())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();

     return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    //4) Method to find customer by email
    @Override
    public ResponseEntity<?> findCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new ApiException(String.format("Customer with email: %s does not exist", email)));
        CustomerResponseDto responseDto = CustomerResponseDto.builder()
                .imageAddress(customer.getImageAddress())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    //5) Method to find all customers
    @Override
    public ResponseEntity<?> findAllCustomers(int pageNum, int pageSize) {
        List<Customer> customerList = customerRepository.findAll();
        if(customerList.isEmpty())
            return new ResponseEntity<>("Customer with email: %s does not exist",
                    HttpStatus.NOT_FOUND);
        List<CustomerResponseDto> responseDtoList = new ArrayList<>();

        customerList.stream().skip(pageNum - 1).limit(pageSize)
                .map(
                        customer -> CustomerResponseDto.builder()
                                .imageAddress(customer.getImageAddress())
                                .firstName(customer.getFirstName())
                                .lastName(customer.getLastName())
                                .build()
                ).forEach(responseDtoList::add);
    //return new ResponseEntity<>(customerList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Customer-Page-Number", String.valueOf(pageNum))
                .header("Customer-Page-Size", String.valueOf(pageSize))
                .header("Customer-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    //6) Method to Update Customer password....email
    public ResponsePojo<CustomerResponseDto> updateCustomer(CustomerUpdateDto updateCustomerDto) {
        //Verify user detail
        Customer customerToUpdate = customerRepository.findByEmail(updateCustomerDto.getEmail())
                .orElseThrow(()-> new ApiException("Customer with the email not found"));

       //Updating fetched customer
         customerToUpdate.setImageAddress(updateCustomerDto.getImageAddress());
         customerToUpdate.setPhoneNumber(updateCustomerDto.getPhoneNumber());
         customerToUpdate.setUsername(updateCustomerDto.getUsername());
         customerToUpdate.setEmail(updateCustomerDto.getEmail());
         customerToUpdate.setPassword(passwordEncoder.encode(updateCustomerDto.getNewPassword()));

        //Saving updated customer details to customer repository
       Customer savedCustomer = customerRepository.save(customerToUpdate);

        CustomerResponseDto customerResponseDto = CustomerResponseDto.builder()
                .imageAddress(savedCustomer.getImageAddress())
                .firstName(savedCustomer.getFirstName())
                .lastName(savedCustomer.getLastName())
                .build();

        //Body of email to send to user's mail
        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(savedCustomer.getEmail())
                .subject("Updated BookReader Account Details")
                .body("Hello " + savedCustomer.getFirstName() + ", " + savedCustomer.getLastName()
                        + "!!\n Your account has been successfully updated.")
                .build();

        //Sending mail to user
        emailService.sendSimpleEmail(emailDetail);

        ResponsePojo<CustomerResponseDto> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.OK);
        responsePojo.setMessage(String.format("Customer with email %s has been updated", updateCustomerDto.getEmail()));
        responsePojo.setData(customerResponseDto);
        return responsePojo;
    }

    //7) Method to search customer's details using multiple parameters
    @Override
    public ResponseEntity<?> searchCustomer(String firstName, String lastName, String username,
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

        //if customerPage is empty
        if(customerPage.isEmpty())
            return new ResponseEntity<>("Nothing found!", HttpStatus.NOT_FOUND);

        //Response POJO
        return new ResponseEntity<>(customerPage, HttpStatus.FOUND);
    }

}
