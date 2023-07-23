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
import org.springframework.util.ObjectUtils;

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

        //If book by the title does not exist already, create a new entry for the book
        if (!likeBookRepository.existsByTitle(likeDto.getTitle())) {

            //If book doesn't exist in the likebook repository, create a new record for username and book title
            LikeBook likeBook = LikeBook.builder()
                    //.username(likeDto.getUsername())
                    .title(likeDto.getTitle())
                    //.likeSpecNum(ResponseUtils.generateUniqueIdentifier(5, likeDto.getTitle()))
                    .likes(true)
                    .totalLikes((long)1)
                    .dateTimeOfLike(LocalDateTime.now())
                    .build();

            likeBookRepository.save(likeBook);
            return new ResponseEntity<>("New like added", HttpStatus.ACCEPTED);
        }

        //If book by the title already exists, edit the details with respect to the like content of likeDto
        //Retrieve the record for book with specified title
        LikeBook userLike = likeBookRepository.findByTitle(likeDto.getTitle()).get();

        //A holder for the total current likes on a book
        long currentLike = userLike.getTotalLikes();

        userLike.setLikes(likeDto.getLikes());
        //Increase or decrease total likes with 'true' or 'false' respectively
        if ((likeDto.getLikes().equals(true))) {
            userLike.setTotalLikes(currentLike + 1);
        } else {
            userLike.setTotalLikes(currentLike - 1);
        }

        userLike.setDateTimeOfLike(LocalDateTime.now());

        //Save updated files to repository
        likeBookRepository.save(userLike);
        return new ResponseEntity<>("Like updated", HttpStatus.ACCEPTED);
    }

    //2) Method to search for Book Likes on book titles
    @Override
    public ResponseEntity<?> titleLikes(String title) {
        //To check if book with the specified title exists in the book database
        if (!bookEntityRepository.existsByTitle(title)) {
            throw new ApiException(String.format("Book by this title: %s does not exist", title));
        }

        //To retrieve all reviews for a book by title
        LikeBook likeBook = likeBookRepository.findByTitle(title).get();

        if(ObjectUtils.isEmpty(likeBook))
            return new ResponseEntity<>(String.format("Likes for book with title: %s not found", title), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(String.format("Likes for book with title %s: %d", title, likeBook.getTotalLikes()), HttpStatus.OK);
    }

    //3) Method to retrieve all book-likes in the database
    @Override
    public ResponseEntity<?> allLikes() {

        //To retrieve all reviews in the review database
        List<LikeBook> likeBookList = likeBookRepository.findAll();
        long totalLikes = likeBookList.stream().mapToLong(LikeBook::getTotalLikes).sum();

        if (likeBookList.isEmpty())
            return new ResponseEntity<>("No likes yet ", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(String.format("Total likes for all books is: %d ", totalLikes), HttpStatus.OK);
    }

}
