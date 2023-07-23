package com.akinnova.bookstoredemo.entity.studententity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    private String level;
    private String projectTitle;
    private String author;
    private String summary;
    private Double price;
    private String serialNumber;
    @CreationTimestamp
    private LocalDateTime supplyDate;
    private Boolean deleteStatus;
}
