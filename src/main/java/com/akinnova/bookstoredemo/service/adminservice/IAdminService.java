package com.akinnova.bookstoredemo.service.adminservice;

import com.akinnova.bookstoredemo.dto.admindto.AdminDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.awt.print.Pageable;
import java.util.List;

public interface IAdminService {
    ResponsePojo<AdminEntity> CreateAdmin (AdminDto adminDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponseEntity<?> findAllAdmin();
    ResponseEntity<?> findAdminByUsername(String username);
    ResponseEntity<?> findAdminByEmail(String email);
    ResponsePojo<AdminEntity> updateAdmin(AdminDto adminDto);
    ResponseEntity<?> deleteAdmin(AdminDto adminDto);
    ResponseEntity<?> searchAdmin(String firstName, String lastName, String username,
                                                String email, String contactNumber);
}
