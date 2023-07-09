package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.dto.AdminDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAdminService {
    ResponsePojo<AdminEntity> searchAdmin(String firstName, String lastName, String username,
                                          String email, String contactNumber);
    ResponsePojo<AdminEntity> CreateAdmin (AdminDto adminDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponsePojo<List<AdminEntity>> findAllAdmin();
    ResponseEntity<?> findAdminByUsername(String username);
    ResponseEntity<?> findAdminByEmail(String email);
    ResponseEntity<?> updateAdmin(AdminDto adminDto);
    ResponseEntity<?> deleteAdmin(AdminDto adminDto);
}
