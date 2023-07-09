package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.repository.RolesRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.dto.AdminDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.entity.Roles;
import com.akinnova.bookstoredemo.repository.AdminRepository;
import com.akinnova.bookstoredemo.response.ResponseUtils;

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

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private EntityManager entityManager;
    private final AdminRepository adminRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //Class Constructor
    public AdminServiceImpl(AdminRepository adminRepository, RolesRepository rolesRepository,
                            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.adminRepository = adminRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;

    }

    @Override
    public ResponsePojo<AdminEntity> searchAdmin(String firstName, String lastName, String username,
                                                 String email, String contactNumber) {

        return null;
    }

    //1) Method create Admin
    public ResponsePojo<AdminEntity> CreateAdmin (AdminDto adminDto){
        if(adminRepository.existsByUsername(adminDto.getUsername())){
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

        //To save roles in the roles database
        Roles roles = Roles.builder()
                .roleName(adminDto.getRole())
                .build();

        //Saves entered role into the database
        rolesRepository.save(roles);

        ResponsePojo<AdminEntity> bookResponsePojo = new ResponsePojo<>();
        bookResponsePojo.setStatusCode(ResponseUtils.CREATED);
        bookResponsePojo.setMessage(String.format(ResponseUtils.CREATED_MESSAGE, adminDto.getFirstName() + " " + adminDto.getLastName()));
        bookResponsePojo.setData(admin);

        return bookResponsePojo;
    }

    //1b) login method
    public ResponseEntity<?> login(LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("Admin logged in successfully", HttpStatus.OK);
    }

    //2) Method to get all Admins
    public ResponsePojo<List<AdminEntity>> findAllAdmin(){
        //List will contain only admins that their active-status are true
        List<AdminEntity> allAdmin = adminRepository.findAll().stream()
                .filter(x -> x.getActiveStatus().equals(true)).collect(Collectors.toList());
        ResponsePojo<List<AdminEntity>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format(ResponseUtils.FOUND_MESSAGE, "All Admins"));
        responsePojo.setData(allAdmin);
        return responsePojo;
    }

    //3) Method to get an Admin
    public ResponseEntity<?> findAdminByUsername(String username){
        Optional<AdminEntity> admin = adminRepository.findByUsername(username);
        admin.orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(admin, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findAdminByEmail(String email) {
        Optional<AdminEntity> admin = adminRepository.findByEmail(email);
        admin.orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(admin, HttpStatus.FOUND);
    }

    //4) Method to update Admin Status
    public ResponseEntity<?> updateAdmin(AdminDto adminDto){
        //Check if Admin exists
        if(!(adminRepository.existsByUsername(adminDto.getUsername())))
            return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);

        //Fetch if admin exists
        Optional<AdminEntity> adminEntityOptional = adminRepository.findByEmail(adminDto.getEmail());
        //Update Admin if Admin exists
         AdminEntity adminToUpdate = adminEntityOptional.get();

         adminToUpdate.setFirstName(adminDto.getFirstName());
         adminToUpdate.setLastName(adminDto.getLastName());
         adminToUpdate.setUsername(adminDto.getUsername());
         adminToUpdate.setEmail(adminDto.getEmail());
         adminToUpdate.setPassword(passwordEncoder.encode(adminDto.getPassword()));
         adminToUpdate.setContactNumber(adminDto.getContactNumber());

        //Saves updated admin details to database
        adminRepository.save(adminToUpdate);

        return new ResponseEntity<>(adminToUpdate, HttpStatus.OK);
    }

    //5) Method to Delete Admin
    public ResponseEntity<?> deleteAdmin(AdminDto adminDto){
        //Check if admin exists
        if(!(adminRepository.existsByEmail(adminDto.getEmail())))
            return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);

        //Fetch admin details to delete
        Optional<AdminEntity> adminEntityOptional = adminRepository.findByUsername(adminDto.getUsername());
        AdminEntity adminToDelete = adminEntityOptional.get();

        //Delete Admin if Admin exists
        adminToDelete.setActiveStatus(false);

        //Saves 'deleted' admin info to database
        adminRepository.save(adminToDelete);
        return new ResponseEntity<>("Admin has been deleted", HttpStatus.OK);

    }
}
