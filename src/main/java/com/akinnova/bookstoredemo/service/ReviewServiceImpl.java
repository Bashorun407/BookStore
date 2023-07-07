package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.ReviewDto;
import com.akinnova.bookstoredemo.entity.Review;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.repository.ReviewRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
@Service
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BookEntityRepository bookEntityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    //1) Method to review book
    @Override
    public ResponseEntity<?> reviewBook(ReviewDto reviewDto) {
        //To check if book with the title exists in the book database
        if(!bookEntityRepository.existsByTitle(reviewDto.getTitle())){
            throw new ApiException(String.format("Book with this title: %s does not exist", reviewDto.getTitle()));
        }

        //To check if user/customer is registered in the database
        if(!customerRepository.existsByUsername(reviewDto.getUsername())){
            throw new ApiException(String.format("User with username: %s does not exist", reviewDto.getUsername()));
        }

        //new review
        Review review = Review.builder()
                .username(reviewDto.getUsername())
                .title(reviewDto.getTitle())
                .likes(likeFunction(reviewDto.getLikes()))
                .starRating(rateFunction(reviewDto.getStarRating()))
                .build();

        //To save review to database
        reviewRepository.save(review);

        //To increase total number of likes if likes was included in the 'payload'
        if(!ObjectUtils.isEmpty(reviewDto.getTitle())){
            review.setTotalLikes(((long) reviewRepository.findByTitle(review.getTitle()).get()
                    .stream().mapToInt(Review::getLikes).sum()));
        }

        //To set average rating if rating was included in the 'payload'
        if(!ObjectUtils.isEmpty(reviewDto.getStarRating())){
            review.setAverageRating((double)reviewRepository.findByTitle(review.getTitle()).get()
                    .stream().mapToInt(Review::getStarRating).sum()
                    /
                    reviewRepository.findByTitle(review.getTitle()).get().size());
        }

        //save totoal likes and star-rating to repository
        reviewRepository.save(review);

        return new ResponseEntity<>("Thanks for your review", HttpStatus.ACCEPTED);
    }

    //1a) Function to set like
    public int likeFunction(int like){

       if(like == 1){
           return 1;
       }
       else
           return 0;
    }

    //1b) Function to set rate
    public int rateFunction(int rate){
        if(rate >= 1  && rate <=5){
            return rate;
        }
        //else return 0
        else
            return 0;
    }


    //2) Method to retrieve reviews on specific book title
    @Override
    public ResponsePojo<List<Review>> titleReviews(String title) {
        //To check if book with the specified title exists in the book database
        if(!bookEntityRepository.existsByTitle(title)){
            throw new ApiException(String.format("Book by this title: %s does not exist", title));
        }

        //To retrieve all reviews for a book by title
        List<Review> reviewList = reviewRepository.findByTitle(title).get();
        ResponsePojo<List<Review>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format("Reviews for %s: ", title));
        responsePojo.setData(reviewList);
        return responsePojo;
    }

    //3) Method to retrieve all reviews in the database
    @Override
    public ResponsePojo<List<Review>> allReviews() {
        //To retrieve all reviews in the review database
        List<Review> reviewList = reviewRepository.findAll();
        ResponsePojo<List<Review>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("All reviews: ");
        responsePojo.setData(reviewList);
        return responsePojo;
    }
}
