package com.akinnova.bookstoredemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "book_store")
public class BookStore {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "book_title", unique = true, nullable = false)
    private String title;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name = "genre", nullable = false)
    private String genre;
    @Column(name = "book_summary")
    private String summary;
    @Column(name = "serial_number")
    private Long serialNumber;
    @Column(name = "volume")
    private Integer volume;
    @Column(name = "quantity")
    private Long quantity;
    @Column(name = "price")
    private Double price;
    @Column(name = "supply_date")
    private Date supplyDate;
    @Column(name = "delete_status")
    private Boolean deleteStatus;

}
