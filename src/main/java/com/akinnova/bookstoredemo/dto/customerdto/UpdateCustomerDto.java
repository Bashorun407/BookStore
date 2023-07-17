package com.akinnova.bookstoredemo.dto.customerdto;

import lombok.Data;

@Data
public class UpdateCustomerDto {
    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;
}
