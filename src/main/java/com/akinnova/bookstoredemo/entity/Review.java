package com.akinnova.bookstoredemo.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

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
    private Boolean likes = false;
    private Integer starRating = 0;
    //@Value("#{${my.int.property:0}}")
    private Long totalLikes;
    //averageRating may or may not contain a double
    private Double averageRating = Double.sum(0, 0);
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "book_review",
            joinColumns = @JoinColumn(name = "review", referencedColumnName = "totalLikes"),
            inverseJoinColumns = @JoinColumn(name = "book", referencedColumnName = "title")
    )
    private List<BookEntity> books;
}
