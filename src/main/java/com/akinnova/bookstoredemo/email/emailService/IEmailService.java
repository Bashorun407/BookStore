package com.akinnova.bookstoredemo.email.emailService;

import com.akinnova.bookstoredemo.email.emailDto.EmailDetail;
import org.springframework.http.ResponseEntity;

public interface IEmailService {
    ResponseEntity<?> sendSimpleEmail(EmailDetail emailDetail);
    ResponseEntity<?> sendEmailWithAttachment(EmailDetail emailDetail);
}
