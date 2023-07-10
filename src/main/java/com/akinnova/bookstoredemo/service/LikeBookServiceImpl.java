package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.LikeDto;
import com.akinnova.bookstoredemo.entity.LikeBook;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.repository.LikeBookRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeBookServiceImpl implements ILikeBookService{

    private final LikeBookRepository likeBookRepository;
    private final BookEntityRepository bookEntityRepository;
    private final CustomerRepository customerRepository;

    //Class Constructor


    public LikeBookServiceImpl(LikeBookRepository likeBookRepository, BookEntityRepository bookEntityRepository,
                               CustomerRepository customerRepository) {
        this.likeBookRepository = likeBookRepository;
        this.bookEntityRepository = bookEntityRepository;
        this.customerRepository = customerRepository;
    }

    //1) Method to generate Like
    @Override
    public ResponseEntity<?> likeBook(LikeDto likeDto) {

        //To check if book with the title exists in the book database
        if(!bookEntityRepository.existsByTitle(likeDto.getTitle())){
            throw new ApiException(String.format("Book with this title: %s does not exist", likeDto.getTitle()));
        }

        //To check if user/customer is registered in the database
        if(!customerRepository.existsByUsername(likeDto.getUsername())){
            throw new ApiException(String.format("User with username: %s does not exist", likeDto.getUsername()));
        }

        //If user has not reviewed any book prior, create a new review for user
        if(!likeBookRepository.existsByUsername(likeDto.getUsername())){
            //new review
            LikeBook review = LikeBook.builder()
                    .username(likeDto.getUsername())
                    .title(likeDto.getTitle())
                    .likes(ResponseUtils.likeFunction(likeDto.getLikes()))
                    .build();

            //To save review to database
            likeBookRepository.save(review);

            //To increase total number of likes if likes was included in the 'payload'
            review.setTotalLikes(((long) likeBookRepository.findByTitle(review.getTitle()).get()
                    .stream().mapToInt(LikeBook::getLikes).sum()));

            //Adding like to total likes after like has been added
            likeBookRepository.save(review);
        }

        //If user has reviewed a book before, retrieve the exact record and edit
        else if (likeBookRepository.existsByUsername(likeDto.getUsername()) && likeBookRepository.existsByTitle(likeDto.getTitle())) {

            LikeBook updatedLike = likeBookRepository.findByTitle(likeDto.getTitle()).get().stream()
                    .filter(x -> x.getUsername().equals(likeDto.getUsername()))
                    .findFirst()
                    .map(review -> {
                        //If user already liked a book and presses like again, it becomes unlike.
                        //Set the like to 0 if re
                        review.setLikes(ResponseUtils.likeFunction(likeDto.getLikes()));
                        if (review.getLikes().equals(1)) {

                            //Decrease total likes by 1
                            review.setTotalLikes(review.getTotalLikes() + 1);
                        } else if(review.getLikes().equals(0)) {

                            //Increase total likes by 1
                            review.setTotalLikes(review.getTotalLikes() - 1);
                        }
                        return review;
                    }).get();

            //Save the change in the Review Repository
            likeBookRepository.save(updatedLike);
        }

        return new ResponseEntity<>("Like added", HttpStatus.GONE);
    }

    //2) Method to search for Book Likes on book titles
    @Override
    public ResponsePojo<List<LikeBook>> titleLikes(String title) {
        //To check if book with the specified title exists in the book database
        if(!bookEntityRepository.existsByTitle(title)){
            throw new ApiException(String.format("Book by this title: %s does not exist", title));
        }

        //To retrieve all reviews for a book by title
        List<LikeBook> likeBookList = likeBookRepository.findByTitle(title).get();

        ResponsePojo<List<LikeBook>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format("Reviews for %s: ", title));
        responsePojo.setData(likeBookList);
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
