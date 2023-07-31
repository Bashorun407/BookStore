package com.akinnova.bookstoredemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "student_project", uniqueConstraints = {
        @UniqueConstraint(columnNames = "serialNumber")
})
public class StudentProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageAddress;
    private String schoolName;
    private String faculty;
    private String department;
    private String projectTitle;
    private String summary;
    private Double price;
    private String serialNumber;
    private String createdBy;
    private String email;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String modifiedBy;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
    private Boolean activeStatus;
}
