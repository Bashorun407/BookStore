package com.akinnova.bookstoredemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "book_review")
public class BookReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //This relationship is intended to get values from 'title' column of BookStore entity
    @JoinColumn(name = "book", referencedColumnName = "title", table = "book_store")
    private BookEntity bookStore;
    private Long like;
    private Long star;
    private String comment;
    @CreationTimestamp
    private LocalDateTime reviewDate;

    //Relationship between book_review and customers
    @ManyToMany
    @JoinTable(name = "book_review",
            joinColumns = @JoinColumn(name = "user_comments", referencedColumnName = "comment"),
            inverseJoinColumns = @JoinColumn(name = "customer", referencedColumnName = "username")
    )
    private List<Customer> customers;

    //Relationship between book_review and books
    @ManyToMany
    @JoinTable(name = "book_rating",
            joinColumns = @JoinColumn(name = "rating", referencedColumnName = "likes"),
            inverseJoinColumns = @JoinColumn(name = "book", referencedColumnName = "title")
    )
    private Set<BookEntity> books;
}
