package com.akinnova.bookstoredemo.service.adminservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.entity.QAdminEntity;
import com.akinnova.bookstoredemo.repository.RolesRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.dto.admindto.AdminDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.entity.Roles;
import com.akinnova.bookstoredemo.repository.AdminRepository;
import com.akinnova.bookstoredemo.response.ResponseUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.awt.print.Pageable;
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

    //1) Method create Admin
    public ResponsePojo<AdminEntity> CreateAdmin (AdminDto adminDto){
        if(adminRepository.existsByUsername(adminDto.getUsername())){
            throw new ApiException("Admin with these details already exits");
        }

        AdminEntity admin = AdminEntity.builder()
                .imageAddress(adminDto.getImageAddress())
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
    public ResponseEntity<?> findAllAdmin(){
        //List will contain only admins that their active-status are true
        List<AdminEntity> allAdmin = adminRepository.findAll().stream()
                .filter(x -> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(allAdmin.isEmpty())
            return new ResponseEntity<>("No Admins found", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(allAdmin, HttpStatus.FOUND);
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
    public ResponsePojo<AdminEntity> updateAdmin(AdminDto adminDto){

        //Fetch if admin exists
        Optional<AdminEntity> adminEntityOptional = adminRepository.findByEmail(adminDto.getEmail());
        adminEntityOptional.orElseThrow(()->
                new ApiException(String.format("Admin with email: %s does not exist", adminDto.getEmail())));
        //Update Admin if Admin exists
         AdminEntity adminToUpdate = adminEntityOptional.get();

         adminToUpdate.setImageAddress(adminDto.getImageAddress());
         adminToUpdate.setFirstName(adminDto.getFirstName());
         adminToUpdate.setLastName(adminDto.getLastName());
         adminToUpdate.setUsername(adminDto.getUsername());
         adminToUpdate.setEmail(adminDto.getEmail());
         adminToUpdate.setPassword(passwordEncoder.encode(adminDto.getPassword()));
         adminToUpdate.setContactNumber(adminDto.getContactNumber());
         adminToUpdate.setActiveStatus(true);

        //Saves updated admin details to database
        adminRepository.save(adminToUpdate);

        //To save roles in the roles database
        Roles roles = Roles.builder()
                .roleName(adminDto.getRole())
                .build();

        //Saves entered role into the database
        rolesRepository.save(roles);

        //Response POJO
        ResponsePojo<AdminEntity> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.ACCEPTED);
        responsePojo.setMessage("Admin detail updated");
        responsePojo.setData(adminToUpdate);

        return responsePojo;
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

    //6) A dynamic search using multiple parameters
    @Override
    public ResponseEntity<?> searchAdmin(String firstName, String lastName, String username,
                                                       String email, String contactNumber) {

        QAdminEntity qAdminEntity = QAdminEntity.adminEntity;
        BooleanBuilder predicate = new BooleanBuilder();

        if(StringUtils.hasText(firstName))
            predicate.and(qAdminEntity.firstName.likeIgnoreCase("%" + firstName + "%"));

        if(StringUtils.hasText(lastName))
            predicate.and(qAdminEntity.lastName.likeIgnoreCase("%" + lastName + "%"));

        if(StringUtils.hasText(username))
            predicate.and(qAdminEntity.username.equalsIgnoreCase(username));

        if(StringUtils.hasText(email))
            predicate.and(qAdminEntity.email.likeIgnoreCase("%" + email + "%"));

        if(StringUtils.hasText(contactNumber))
            predicate.and(qAdminEntity.contactNumber.eq(contactNumber));

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<AdminEntity> jpaQuery = jpaQueryFactory.selectFrom(qAdminEntity)
                .where(predicate.and(qAdminEntity.activeStatus.eq(true)))
                .orderBy(qAdminEntity.id.asc());
//                .offset(pageable.getNumberOfPages())
//                .limit(pageable.getNumberOfPages());

        Page<AdminEntity> adminEntityPage = new PageImpl<>(jpaQuery.fetch());

        if(adminEntityPage.isEmpty())
            return new ResponseEntity<>("Your search does not match any item", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(adminEntityPage, HttpStatus.OK);
    }
}
