package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.response.BookResponsePojo;
import com.akinnova.bookstoredemo.dto.AdminDto;
import com.akinnova.bookstoredemo.dto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //1) Method create Admin
    @PostMapping("/createAdmin")
    public BookResponsePojo<AdminEntity> CreateAdmin (@RequestBody AdminDto adminDto){
        return adminService.CreateAdmin(adminDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return adminService.login(loginDto);
    }

    //2) Method to get all Admins
    @GetMapping("/allAdmin")
    public ResponseEntity<?> findAllAdmin(){
        return adminService.findAllAdmin();
    }

    //3) Method to get an Admin
    @GetMapping("/findAdmin/{search}")
    public ResponseEntity<?> findAdminByUsernameOrEmail(@PathVariable(value = "search") String username,
                                                        @PathVariable(value = "search") String email){
        return adminService.findAdminByUsernameOrEmail(username, email);
    }

    //4) Method to update Admin Status
    @PutMapping("/updateAdmin")
    public ResponseEntity<?> updateAdmin(@RequestBody AdminDto adminDto){
        return adminService.updateAdmin(adminDto);
    }

    //5) Method to Delete Admin
    @DeleteMapping("/deleteAdmin")
    public ResponseEntity<?> deleteAdmin(@RequestBody AdminDto adminDto){
        return adminService.deleteAdmin(adminDto);
    }
}
