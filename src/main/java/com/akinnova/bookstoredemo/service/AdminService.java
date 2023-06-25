package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.repository.RolesRepository;
import com.akinnova.bookstoredemo.response.BookResponsePojo;
import com.akinnova.bookstoredemo.dto.AdminDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.entity.Roles;
import com.akinnova.bookstoredemo.repository.AdminRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;


    //1) Method create Admin
    public BookResponsePojo<AdminEntity> CreateAdmin (AdminDto adminDto){
        if(adminRepository.existsByUsernameOrEmail(adminDto.getUsername(), adminDto.getEmail())){
            throw new ApiException("Admin with these details already exits");
        }

        AdminEntity admin = AdminEntity.builder()
                .firstName(adminDto.getFirstName())
                .lastName(adminDto.getLastName())
                .username(adminDto.getUsername())
                .email(adminDto.getEmail())
                .password(passwordEncoder.encode(adminDto.getPassword()))
                .contactNumber(adminDto.getContactNumber())
                .activeStatus(true)
                .build();

        //Saves newly entered admin record into the database
        adminRepository.save(admin);

        //To confirm that roles exist in the database
//        rolesRepository.findByRoleName(adminDto.getRole())
//                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));

        Roles roles = Roles.builder()
                .roleName(adminDto.getRole())
                .build();

        //Saves entered role into the database
        rolesRepository.save(roles);

        //Saves newly entered admin record into the database
        adminRepository.save(admin);

        BookResponsePojo<AdminEntity> bookResponsePojo = new BookResponsePojo<>();
        bookResponsePojo.setStatusCode("200");
        bookResponsePojo.setMessage("Admin Created");
        bookResponsePojo.setData(admin);

        return bookResponsePojo;
    }

    //1b) login
    public ResponseEntity<?> login(LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
    }

    //2) Method to get all Admins
    public ResponseEntity<?> findAllAdmin(){
        //List will contain only admins that their active-status are true
        List<AdminEntity> allAdmin = adminRepository.findAll().stream()
                .filter(x -> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        return new ResponseEntity<>(allAdmin, HttpStatus.OK);
    }

    //3) Method to get an Admin
    public ResponseEntity<?> findAdminByUsernameOrEmail(String username, String email){
        Optional<AdminEntity> admin = adminRepository.findByUsernameOrEmail(username, email);
        admin.orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(admin, HttpStatus.FOUND);
    }

    //4) Method to update Admin Status
    public ResponseEntity<?> updateAdmin(AdminDto adminDto){
        //Check if Admin exists
        if(!(adminRepository.existsByUsernameOrEmail(adminDto.getUsername(), adminDto.getEmail())))
            return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);
        //Update Admin if Admin exists
        AdminEntity admin = AdminEntity.builder()
                .firstName(adminDto.getFirstName())
                .lastName(adminDto.getLastName())
                .username(adminDto.getUsername())
                .email(adminDto.getEmail())
                .password(passwordEncoder.encode(adminDto.getPassword()))
                .contactNumber(adminDto.getContactNumber())
                .build();

        //Saves updated admin details to database
        adminRepository.save(admin);

        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    //5) Method to Delete Admin
    public ResponseEntity<?> deleteAdmin(AdminDto adminDto){
        //Check if admin exists
        if(!(adminRepository.existsByUsernameOrEmail(adminDto.getUsername(), adminDto.getEmail())))
            return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);

        //Delete Admin if Admin exists
        AdminEntity admin = AdminEntity.builder()
                .activeStatus(false)
                .build();

        //Saves 'deleted' admin info to database
        return new ResponseEntity<>("Admin has been deleted", HttpStatus.OK);

    }
}
