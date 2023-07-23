package com.akinnova.bookstoredemo.dto.customerdto;

import lombok.Data;

@Data
public class CustomerUpdateDto {
    private String imageAddress;
    private String phoneNumber;
    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;
}
