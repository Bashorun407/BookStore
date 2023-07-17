package com.akinnova.bookstoredemo.controller.admincontroller;

import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.dto.admindto.AdminDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.entity.AdminEntity;
import com.akinnova.bookstoredemo.service.adminservice.AdminServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@AllArgsConstructor
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

    //6) A dynamic search using multiple parameters
    @GetMapping("/search")
    public ResponsePojo<Page<AdminEntity>> searchAdmin(@RequestParam(name = "firstName", required = false) String firstName,
                                                       @RequestParam(name = "lastName", required = false) String lastName,
                                                       @RequestParam(name = "username", required = false) String username,
                                                       @RequestParam(name = "email", required = false) String email,
                                                       @RequestParam(name = "contactNumber", required = false) String contactNumber,
                                                       Pageable pageable) {
        return adminService.searchAdmin(firstName, lastName, username, email, contactNumber, pageable);
    }
}
