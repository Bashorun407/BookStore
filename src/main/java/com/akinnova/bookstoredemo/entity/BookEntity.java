package com.akinnova.bookstoredemo.entity;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "book_table", uniqueConstraints = {
        @UniqueConstraint(columnNames = "serialNumber")
})
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageAddress;
    private String title;
    private String author;
    private String genre;
    private String summary;
    private String serialNumber;
    private String edition;
    private Integer volume;
    private Long quantity;
    private Double price;
    @CreationTimestamp
    private LocalDateTime supplyDate;
    private Boolean deleteStatus;

}
