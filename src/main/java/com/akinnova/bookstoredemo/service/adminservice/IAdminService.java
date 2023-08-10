package com.akinnova.bookstoredemo.service.adminservice;

import com.akinnova.bookstoredemo.dto.admindto.AdminDto;
import com.akinnova.bookstoredemo.dto.admindto.AdminResponseDto;
import com.akinnova.bookstoredemo.dto.admindto.AdminUpdateDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;


public interface IAdminService {
    ResponsePojo<AdminResponseDto> CreateAdmin (AdminDto adminDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponseEntity<?> findAllAdmin(int pageNum, int pageSize);
    ResponseEntity<?> findAdminByUsername(String username);
    ResponseEntity<?> findAdminByEmail(String email);
    ResponseEntity<?> updateAdmin(AdminUpdateDto adminDto);
    ResponseEntity<?> deleteAdmin(AdminDto adminDto);
    ResponseEntity<?> searchAdmin(String firstName, String lastName, String username,
                                                String email, String contactNumber, int pageNum, int pageSize);
}
