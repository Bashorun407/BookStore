package com.akinnova.bookstoredemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "book_store", uniqueConstraints = {
        //@UniqueConstraint(columnNames = "book_title"),
        @UniqueConstraint(columnNames = "serialNumber")
})
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String summary;
    private String serialNumber;
    private Integer volume;
    private Long quantity;
    private Double price;
    @CreationTimestamp
    private LocalDateTime supplyDate;
    @Value("false")
    private Boolean deleteStatus;

}
