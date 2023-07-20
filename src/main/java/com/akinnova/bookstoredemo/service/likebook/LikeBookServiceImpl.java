package com.akinnova.bookstoredemo.service.likebook;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.likedto.LikeDto;
import com.akinnova.bookstoredemo.entity.LikeBook;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.LikeBookRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeBookServiceImpl implements ILikeBookService {

    private final LikeBookRepository likeBookRepository;
    private final BookEntityRepository bookEntityRepository;

    //Class Constructor
    public LikeBookServiceImpl(LikeBookRepository likeBookRepository, BookEntityRepository bookEntityRepository) {
        this.likeBookRepository = likeBookRepository;
        this.bookEntityRepository = bookEntityRepository;

    }

    //1) Method to like/or unlike a  book
    @Override
    public ResponseEntity<?> likeBook(LikeDto likeDto) {

        //Check if the book exists
        if (!bookEntityRepository.existsByTitle(likeDto.getTitle())) {
            return new ResponseEntity<>("Book by title: " + likeDto.getTitle() + " does not exist.", HttpStatus.NOT_FOUND);
        }

        //If book by the title does not exist already, create a new entry for the book
        if (!likeBookRepository.existsByTitle(likeDto.getTitle())) {

            //If book doesn't exist in the likebook repository, create a new record for username and book title
            LikeBook likeBook = LikeBook.builder()
                    //.username(likeDto.getUsername())
                    .title(likeDto.getTitle())
                    //.likeSpecNum(ResponseUtils.generateUniqueIdentifier(5, likeDto.getTitle()))
                    .likes(true)
                    .totalLikes(Long.sum(0, 1))
                    .dateTimeOfLike(LocalDateTime.now())
                    .build();

            likeBookRepository.save(likeBook);
            return new ResponseEntity<>("Like added", HttpStatus.ACCEPTED);
        }

        //If book by the title already exists, edit the details with respect to the like content of likeDto

        //Retrieve the record for book with specified title
        LikeBook likeBook = likeBookRepository.findByTitle(likeDto.getTitle()).get();

        //if book exists...and like == true, increment total like for the book
        if (likeDto.getLikes().equals(true)) {

            likeBook.setLikes(true);
            likeBook.setTotalLikes(likeBook.getTotalLikes() + 1);
            likeBook.setDateTimeOfLike(LocalDateTime.now());
        }

        //if book exists... and like == false, decrease total like for the book
        else {
            likeBook.setLikes(false);
            likeBook.setTotalLikes(likeBook.getTotalLikes() - 1);
            likeBook.setDateTimeOfLike(LocalDateTime.now());
        }

        //Save updated files to repository
        likeBookRepository.save(likeBook);
        return new ResponseEntity<>("Like updated", HttpStatus.ACCEPTED);
    }

    //2) Method to search for Book Likes on book titles
    @Override
    public ResponsePojo<LikeBook> titleLikes(String title) {
        //To check if book with the specified title exists in the book database
        if (!bookEntityRepository.existsByTitle(title)) {
            throw new ApiException(String.format("Book by this title: %s does not exist", title));
        }

        //To retrieve all reviews for a book by title
        LikeBook likeBook = likeBookRepository.findByTitle(title).get();

        ResponsePojo<LikeBook> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format("Reviews for %s: ", title));
        responsePojo.setData(likeBook);
        return responsePojo;
    }

    //3) Method to retrieve all book-likes in the database
    @Override
    public ResponsePojo<List<LikeBook>> allLikes() {

        //To retrieve all reviews in the review database
        List<LikeBook> likeBookList = likeBookRepository.findAll();

        ResponsePojo<List<LikeBook>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("All reviews: ");
        responsePojo.setData(likeBookList);
        return responsePojo;
    }

}