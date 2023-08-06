package com.akinnova.bookstoredemo.dto.customerdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponseDto {
    private String imageAddress;
    private String firstName;
    private String lastName;
}
