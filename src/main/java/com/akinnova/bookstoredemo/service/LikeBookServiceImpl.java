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
import java.util.stream.Collectors;

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

        //Check the repository if there is already a comment by username on the topic
        List<LikeBook> likeBookList = likeBookRepository.findByTitle(likeDto.getTitle()).get().stream()
                .filter(x -> x.getUsername().equals(likeDto.getUsername())).collect(Collectors.toList());

        //If user has not reviewed any book prior, create a new review for user
        //(i.e.) User does not exist and book title does not already exist
        if(likeBookList.isEmpty()){
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

        //For a user to set like for another book title even if they have liked another book
        //If user has reviewed a book before, retrieve the exact record and edit
        //(i.e. username exists but title does not)


        //If user has reviewed a book before, retrieve the exact record for that book and edit it
        //(i.e. Username exists and title exists)
        else if (!likeBookList.isEmpty()) {

            LikeBook updatedLike = likeBookRepository.findByTitle(likeDto.getTitle()).get().stream()
                    .filter(x -> x.getUsername().equals(likeDto.getUsername()))
                    .findFirst()
                    .map(review -> {

                        //if review.getLike's value is 1, increment setTotalLikes
                        if (review.getLikes().equals(1)) {
                            //Set the value of like as the value the likeDto passes
                            review.setLikes(ResponseUtils.likeFunction(likeDto.getLikes()));

                            //Save the change in the Review Repository
                            likeBookRepository.save(review);
                            //review.setTotalLikes(review.getTotalLikes() + 1);

                            //Set total likes
                            review.setTotalLikes(((long) likeBookRepository.findByTitle(review.getTitle()).get()
                                    .stream().mapToInt(LikeBook::getLikes).sum()));


                        } else if(review.getLikes().equals(0)) {

                            //Set the value of like as the value the likeDto passes
                            review.setLikes(ResponseUtils.likeFunction(likeDto.getLikes()));

                            //Save the change in the Review Repository
                            likeBookRepository.save(review);

                            //Set total likes
                            review.setTotalLikes(((long) likeBookRepository.findByTitle(review.getTitle()).get()
                                    .stream().mapToInt(LikeBook::getLikes).sum()) - 1);
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
