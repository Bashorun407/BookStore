package com.akinnova.bookstoredemo.entity.postgraduate;

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
@Table(name = "post_graduate_thesis", uniqueConstraints = {
        @UniqueConstraint(columnNames = "serialNumber")
})
public class PostGraduateThesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageAddress;
    private String schoolName;
    private String faculty;
    private String department;
    private String level;
    private String thesisTitle;
    private String author;
    private String summary;
    private String serialNumber;
    private Long quantity;
    private Double price;
       @CreationTimestamp
    private LocalDateTime supplyDate;
    private Boolean deleteStatus;
}
