package com.akinnova.bookstoredemo.dto.logindto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String email;
    private String password;
}
