package com.akinnova.bookstoredemo.email.emailDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDetail {
    private String recipient;
    private String subject;
    private String body;
    private String filePath;

}
