package com.akinnova.bookstoredemo.email.emailService;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.email.emailDto.EmailDetail;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
@AllArgsConstructor
@Service
public class EmailService implements IEmailService{
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderMail;
    @Override
    public ResponseEntity<?> sendSimpleEmail(EmailDetail emailDetail) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderMail);
            simpleMailMessage.setTo(emailDetail.getRecipient());
            simpleMailMessage.setSubject(emailDetail.getSubject());
            simpleMailMessage.setText(emailDetail.getBody());

            //Here javaMailSender sends the mail
            javaMailSender.send(simpleMailMessage);

        }
        catch (MailException me){
            throw new ApiException("error sending mail: " + me);
        }
        return new ResponseEntity<>("Mail sent successfully", HttpStatus.GONE);
    }

    @Override
    public ResponseEntity<?> sendEmailWithAttachment(EmailDetail emailDetail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(senderMail);
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            mimeMessageHelper.setText(emailDetail.getBody());

            FileSystemResource fileSystemResource = new FileSystemResource(new File(emailDetail.getFilePath()));
            mimeMessageHelper.addAttachment(emailDetail.getFilePath(), fileSystemResource);

            //Here javaMailSender sends the mail
            javaMailSender.send(mimeMessage);
        }
        catch (MailException me){
            throw new RuntimeException("Error sending mail with attachment: " + me);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>("Mime mail sent successfully", HttpStatus.GONE);
    }
}
