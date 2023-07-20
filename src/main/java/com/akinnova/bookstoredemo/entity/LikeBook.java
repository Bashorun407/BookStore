package com.akinnova.bookstoredemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "book_likes")
public class LikeBook implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Boolean likes = true;
    private Long totalLikes;
    private LocalDateTime dateTimeOfLike;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "book_likes_review",
            joinColumns = @JoinColumn(name = "likes", referencedColumnName = "totalLikes"),
            inverseJoinColumns = @JoinColumn(name = "book", referencedColumnName = "title")
    )
    private List<BookEntity> books;
}
