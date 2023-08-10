package com.akinnova.bookstoredemo.controller.admincontroller;

import com.akinnova.bookstoredemo.dto.admindto.AdminResponseDto;
import com.akinnova.bookstoredemo.dto.admindto.AdminUpdateDto;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.dto.admindto.AdminDto;
import com.akinnova.bookstoredemo.dto.logindto.LoginDto;
import com.akinnova.bookstoredemo.service.adminservice.AdminServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;

    //1) Method create Admin
    @PostMapping("/createAdmin")
    public ResponsePojo<AdminResponseDto> CreateAdmin (@RequestBody AdminDto adminDto){
        return adminService.CreateAdmin(adminDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return adminService.login(loginDto);
    }

    //2) Method to get all Admins
    @GetMapping("/allAdmin")
    public ResponseEntity<?> findAllAdmin(int pageNum, int pageSize){
        return adminService.findAllAdmin(pageNum, pageSize);
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
    public ResponseEntity<?> updateAdmin(@RequestBody AdminUpdateDto adminDto){
        return adminService.updateAdmin(adminDto);
    }

    //5) Method to Delete Admin
    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteAdmin(@RequestBody AdminDto adminDto){
        return adminService.deleteAdmin(adminDto);
    }

    //6) A dynamic search using multiple parameters
    @GetMapping("/search")
    public ResponseEntity<?> searchAdmin(@RequestParam(name = "firstName", required = false) String firstName,
                                                       @RequestParam(name = "lastName", required = false) String lastName,
                                                       @RequestParam(name = "username", required = false) String username,
                                                       @RequestParam(name = "email", required = false) String email,
                                                       @RequestParam(name = "contactNumber", required = false) String contactNumber,
                                                        @RequestParam(name = "pageNum", required = false) int pageNum,
                                                        @RequestParam(name = "pageSize", required = false) int pageSize)
    {
        return adminService.searchAdmin(firstName, lastName, username, email, contactNumber, pageNum, pageSize);
    }
}
