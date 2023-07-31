package com.akinnova.bookstoredemo.entity;

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
@Table(name = "handout", uniqueConstraints = {
        @UniqueConstraint(columnNames = "courseCode"),
        @UniqueConstraint(columnNames = "courseTitle"),
        @UniqueConstraint(columnNames = "serialNumber")
})
public class  HandOut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageAddress;
    private String schoolName;
    private String faculty;
    private String department;
    private String level;
    private String courseCode;
    private String courseTitle;
    private String summary;
    private String serialNumber;
    @CreationTimestamp
    private LocalDateTime supplyDate;
    private Boolean deleteStatus;

}
