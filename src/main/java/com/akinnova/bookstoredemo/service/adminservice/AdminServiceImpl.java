package com.akinnova.bookstoredemo.service.adminservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.admindto.AdminResponseDto;
import com.akinnova.bookstoredemo.dto.admindto.AdminUpdateDto;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
@Service
public class AdminServiceImpl implements IAdminService {

    private final EntityManager entityManager;
    private final AdminRepository adminRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //Class Constructor
    public AdminServiceImpl(EntityManager entityManager, AdminRepository adminRepository, RolesRepository rolesRepository,
                            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.entityManager = entityManager;
        this.adminRepository = adminRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;

    }

    //1) Method create Admin
    public ResponsePojo<AdminResponseDto> CreateAdmin (AdminDto adminDto){
        boolean check = adminRepository.existsByUsername(adminDto.getUsername());
        if(check){
            throw new ApiException("Admin with these details already exits");
        }

        AdminEntity admin = AdminEntity.builder()
                .imageAddress(adminDto.getImageAddress())
                .firstName(adminDto.getFirstName())
                .lastName(adminDto.getLastName())
                .role(adminDto.getRole())
                .username(adminDto.getUsername())
                .email(adminDto.getEmail())
                .password(passwordEncoder.encode(adminDto.getPassword()))
                .contactNumber(adminDto.getContactNumber())
                .activeStatus(true)
                .build();

        //Saves newly entered admin record into the database
        adminRepository.save(admin);

        AdminResponseDto responseDto = AdminResponseDto.builder()
                .imageAddress(adminDto.getImageAddress())
                .firstName(adminDto.getFirstName())
                .lastName(adminDto.getLastName())
                .role(adminDto.getRole())
                .build();
        //To save roles in the roles database
        Roles roles = Roles.builder()
                .roleName(adminDto.getRole())
                .build();

        //Saves entered role into the database
        rolesRepository.save(roles);

        ResponsePojo<AdminResponseDto> bookResponsePojo = new ResponsePojo<>();
        bookResponsePojo.setStatusCode(ResponseUtils.CREATED);
        bookResponsePojo.setMessage(String.format(ResponseUtils.CREATED_MESSAGE, adminDto.getFirstName() + " " + adminDto.getLastName()));
        bookResponsePojo.setData(responseDto);

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
    public ResponseEntity<?> findAllAdmin(int pageNum, int pageSize){
        //List will contain only admins that their active-status are true
        List<AdminEntity> allAdmin = adminRepository.findAll().stream()
                .skip(pageNum - 1).limit(pageSize)
                .filter(x -> x.getActiveStatus().equals(true)).toList();

        List<AdminResponseDto> responseDtoList = new ArrayList<>();

        if(allAdmin.isEmpty())
            return new ResponseEntity<>("No Admins found", HttpStatus.NOT_FOUND);

        //Admin response dto
        allAdmin.stream().map(
                adminEntity -> AdminResponseDto.builder()
                        .imageAddress(adminEntity.getImageAddress())
                        .firstName(adminEntity.getFirstName())
                        .lastName(adminEntity.getLastName())
                        .role(adminEntity.getRole())
                        .build()
        ).forEach(responseDtoList::add);

        // Return the pagedAdminList and the total count of admins
        return ResponseEntity.ok()
                .header("Admin-Page-Number", String.valueOf(pageNum))
                .header("Admin-Page-Size", String.valueOf(pageSize))
                .header("Admin-Total-Count", String.valueOf(allAdmin.size()))
                .body(responseDtoList);
    }

    //3) Method to get an Admin
    public ResponseEntity<?> findAdminByUsername(String username){
        AdminEntity admin = adminRepository.findByUsername(username)
                .orElseThrow(()-> new ApiException(String.format("Admin with username: %s does not exist.", username)));

        //Response dto
        AdminResponseDto responseDto = AdminResponseDto.builder()
                .imageAddress(admin.getImageAddress())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .role(admin.getRole())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findAdminByEmail(String email) {
        AdminEntity admin = adminRepository.findByEmail(email)
               .orElseThrow(()-> new ApiException(String.format("Admin with username: %s does not exist.", email)));

        //Response dto
        AdminResponseDto responseDto = AdminResponseDto.builder()
                .imageAddress(admin.getImageAddress())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .role(admin.getRole())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    //4) Method to update Admin Status
    public ResponseEntity<?> updateAdmin(AdminUpdateDto adminDto){

        //Fetch if admin exists
        AdminEntity adminToUpdate = adminRepository.findByEmail(adminDto.getEmail())
                .orElseThrow(()->
                new ApiException(String.format("Admin with email: %s does not exist", adminDto.getEmail())));

         adminToUpdate.setImageAddress(adminDto.getImageAddress());
         adminToUpdate.setUsername(adminDto.getUsername());
         adminToUpdate.setEmail(adminDto.getEmail());
         adminToUpdate.setPassword(passwordEncoder.encode(adminDto.getPassword()));
         adminToUpdate.setContactNumber(adminDto.getContactNumber());
         adminToUpdate.setActiveStatus(true);

        //Saves updated admin details to database
        adminRepository.save(adminToUpdate);

        //Response POJO

        return new ResponseEntity<>("Admin updated successfully", HttpStatus.ACCEPTED);
    }

    //5) Method to Delete Admin
    public ResponseEntity<?> deleteAdmin(AdminDto adminDto){

        //Fetch admin details to delete
        AdminEntity adminToDelete = adminRepository.findByUsername(adminDto.getUsername())
                .filter(AdminEntity::getActiveStatus)
                .orElseThrow(()-> new ApiException(String.format("Admin with username: %s not found.",
                adminDto.getUsername())));

        //Delete Admin if Admin exists
        adminToDelete.setActiveStatus(false);

        //Saves 'deleted' admin info to database
        adminRepository.save(adminToDelete);
        return new ResponseEntity<>("Admin has been deleted", HttpStatus.OK);

    }

    //6) A dynamic search using multiple parameters
    @Override
    public ResponseEntity<?> searchAdmin(String firstName, String lastName, String username,
                                                       String email, String contactNumber, int pageNum, int pageSize) {

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
                .orderBy(qAdminEntity.id.asc())
                .offset(pageNum)
                .limit(pageSize);

        Page<AdminEntity> adminEntityPage = new PageImpl<>(jpaQuery.fetch());

        if(adminEntityPage.isEmpty())
            return new ResponseEntity<>("Your search does not match any item", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(adminEntityPage, HttpStatus.OK);
    }
}
