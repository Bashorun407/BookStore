package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.dto.AdminDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.awt.print.Pageable;
import java.util.List;

public interface IAdminService {
    ResponsePojo<AdminEntity> CreateAdmin (AdminDto adminDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponsePojo<List<AdminEntity>> findAllAdmin();
    ResponseEntity<?> findAdminByUsername(String username);
    ResponseEntity<?> findAdminByEmail(String email);
    ResponseEntity<?> updateAdmin(AdminDto adminDto);
    ResponseEntity<?> deleteAdmin(AdminDto adminDto);
    ResponsePojo<Page<AdminEntity>> searchAdmin(String firstName, String lastName, String username,
                                                String email, String contactNumber, Pageable pageable);
}
