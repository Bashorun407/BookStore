package com.akinnova.bookstoredemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "review")
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String title;
    private Integer likes ;
    private Integer starRating ;
    private Long totalLikes ;
    //averageRating may or may not contain a double
    private Double averageRating;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "book_review",
            joinColumns = @JoinColumn(name = "review", referencedColumnName = "totalLikes"),
            inverseJoinColumns = @JoinColumn(name = "book", referencedColumnName = "title")

    )
    private List<BookEntity> books;
}
