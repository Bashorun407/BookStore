package com.akinnova.bookstoredemo.service.likebook;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.likedto.LikeDto;
import com.akinnova.bookstoredemo.entity.LikeBook;
import com.akinnova.bookstoredemo.repository.LikeBookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeBookServiceImpl implements ILikeBookService {

    private final LikeBookRepository likeBookRepository;

    //Class Constructor
    public LikeBookServiceImpl(LikeBookRepository likeBookRepository) {
        this.likeBookRepository = likeBookRepository;

    }

    //1) Method to like/or unlike a  book
    @Override
    public ResponseEntity<?> likeBook(LikeDto likeDto) {
        LikeBook likeBook = likeBookRepository.findByTitle(likeDto.getTitle())
                        .orElse(
                                likeBookRepository.save(LikeBook.builder().title(likeDto.getTitle())
                                        .likes(likeDto.getLikes())
                                        .totalLikes((long)1).dateTimeOfLike(LocalDateTime.now()).build()));
        //Increment existing likes
        likeBook.setLikes(likeDto.getLikes());
        likeBook.setTotalLikes(likeBook.getTotalLikes() + 1);
        likeBook.setDateTimeOfLike(LocalDateTime.now());
        likeBookRepository.save(likeBook);
        return new ResponseEntity<>("Like updated", HttpStatus.ACCEPTED);
    }

    //2) Method to search for Book Likes on book titles
    @Override
    public ResponseEntity<?> titleLikes(String title) {

        //To retrieve all reviews for a book by title
        LikeBook likeBook = likeBookRepository.findByTitle(title)
                .orElseThrow(()-> new ApiException(String.format("Likes for book with title: %s not found", title)));

        return new ResponseEntity<>(String.format("Likes for book with title %s: %d", title, likeBook.getTotalLikes()), HttpStatus.OK);
    }

    //3) Method to retrieve all book-likes in the database
    @Override
    public ResponseEntity<?> allLikes(int pageNum, int pageSize) {

        //To retrieve all reviews in the review database
        List<LikeBook> likeBookList = likeBookRepository.findAll().stream()
                .skip(pageNum - 1).limit(pageSize).toList();

        //return new ResponseEntity<>(String.format("Total likes for all books is: %d ", totalLikes), HttpStatus.OK);
        return ResponseEntity.ok()
                .header("Likes-Page-Number", String.valueOf(pageNum))
                .header("Likes-Page-Size", String.valueOf(pageSize))
                .header("Likes-Total-Count", String.valueOf(likeBookList.size()))
                .body(likeBookList);
    }

}
