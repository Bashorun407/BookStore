package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.dto.AdminDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.service.AdminServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminService;

    //1) Method create Admin
    @PostMapping("/createAdmin")
    public ResponsePojo<AdminEntity> CreateAdmin (@RequestBody AdminDto adminDto){
        return adminService.CreateAdmin(adminDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return adminService.login(loginDto);
    }

    //2) Method to get all Admins
    @GetMapping("/allAdmin")
    public ResponsePojo<List<AdminEntity>> findAllAdmin(){
        return adminService.findAllAdmin();
    }

    //3) Method to get an Admin
    @GetMapping("/adminByUsername/{search}")
    public ResponseEntity<?> findAdminByUsername(@PathVariable(value = "search") String username){
        return adminService.findAdminByUsername(username);
    }

    //3) Method to get an Admin
    @GetMapping("/adminByEmail/{search}")
    public ResponseEntity<?> findAdminByEmail(@PathVariable(value = "search") String email) {
        return adminService.findAdminByEmail(email);
    }


    //4) Method to update Admin Status
    @PutMapping("/admin")
    public ResponseEntity<?> updateAdmin(@RequestBody AdminDto adminDto){
        return adminService.updateAdmin(adminDto);
    }

    //5) Method to Delete Admin
    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteAdmin(@RequestBody AdminDto adminDto){
        return adminService.deleteAdmin(adminDto);
    }
}
