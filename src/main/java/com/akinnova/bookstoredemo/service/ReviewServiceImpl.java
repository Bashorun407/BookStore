package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.LikeDto;
import com.akinnova.bookstoredemo.dto.RateDto;
import com.akinnova.bookstoredemo.entity.Review;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.repository.ReviewRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReviewServiceImpl implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final BookEntityRepository bookEntityRepository;
    private final CustomerRepository customerRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, BookEntityRepository bookEntityRepository,
                             CustomerRepository customerRepository) {
        this.reviewRepository = reviewRepository;
        this.bookEntityRepository = bookEntityRepository;
        this.customerRepository = customerRepository;
    }


    //1) Method to like book
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

        //If the book has been reviewed by user prior...search for review in the review repository
        List<Review> userReviewList = reviewRepository.findByUsername(likeDto.getUsername()).get();

        //If user has not reviewed any book prior, create a new review for user
        if(userReviewList.isEmpty()){
            //new review
            Review review = Review.builder()
                    .username(likeDto.getUsername())
                    .title(likeDto.getTitle())
                    .likes(ResponseUtils.likeFunction(likeDto.getLikes()))
                    .build();

            //To save review to database
            reviewRepository.save(review);

            //To increase total number of likes if likes was included in the 'payload'
            review.setTotalLikes(((long) reviewRepository.findByTitle(review.getTitle()).get()
                    .stream().mapToInt(Review::getLikes).sum()));

            //Adding like to total likes after like has been added
            reviewRepository.save(review);
        }

        //If user has reviewed a book before, retrieve the exact record and edit
        else if (!userReviewList.isEmpty()) {
            Review updatedReview = reviewRepository.findByTitle(likeDto.getTitle()).get().stream()
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
            reviewRepository.save(updatedReview);
        }

        return new ResponseEntity<>("Like added", HttpStatus.GONE);
    }


    //2) Method to rate book...(Rate should be between 1 and 5)
    @Override
    public ResponseEntity<?> rateBook(RateDto rateDto) {

        //To check if book with the title exists in the book database
        if(!bookEntityRepository.existsByTitle(rateDto.getTitle())){
            throw new ApiException(String.format("Book with this title: %s does not exist", rateDto.getTitle()));
        }

        //To check if user/customer is registered in the database
        if(!customerRepository.existsByUsername(rateDto.getUsername())){
            throw new ApiException(String.format("User with username: %s does not exist", rateDto.getUsername()));
        }

        //If the book has been reviewed by user prior...search for review in the review repository
        List<Review> userReviewList = reviewRepository.findByUsername(rateDto.getUsername()).get();

        //If user has not reviewed any book prior, create a new review for user
        if(userReviewList.isEmpty()){
            //new review
            Review review = Review.builder()
                    .username(rateDto.getUsername())
                    .title(rateDto.getTitle())
                    .starRating(ResponseUtils.rateFunction(rateDto.getStarRating()))
                    .build();

            //To save review to database
            reviewRepository.save(review);

            //To set average rating if rating was included in the 'payload'
            review.setAverageRating((double)reviewRepository.findByTitle(review.getTitle()).get()
                    .stream().mapToInt(Review::getStarRating).sum()
                    /
                    reviewRepository.findByTitle(review.getTitle()).get().size());

            //To save review to database
            reviewRepository.save(review);
            //return new ResponseEntity<>("Thanks for your new review.", HttpStatus.ACCEPTED);
        }

        //If user has reviewed a book before, retrieve the exact record and edit
        else if(!userReviewList.isEmpty()) {

            Review updatedReview = reviewRepository.findByTitle(rateDto.getTitle()).get().stream()
                    .filter(x-> x.getUsername().equals(rateDto.getUsername()))
                    .findFirst()
                    .map(review -> {
                        review.setStarRating(ResponseUtils.rateFunction(rateDto.getStarRating()));
                        review.setAverageRating(
                                (double) reviewRepository.findByTitle(rateDto.getTitle()).get().stream()
                                        .mapToInt(Review::getStarRating).sum()
                                        /
                                        reviewRepository.findByTitle(rateDto.getTitle()).get().size()
                        );

                        //returns review to its caller
                        return review;
                    }).get();

            //Save update into the database
            reviewRepository.save(updatedReview);
        }

        return new ResponseEntity<>("Thanks for your review", HttpStatus.ACCEPTED);
    }


    //3) Method to retrieve reviews on specific book title
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


    //4) Method to retrieve all reviews in the database
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


    //1) Method to review book
//    @Override
//    public ResponseEntity<?> reviewBook(ReviewDto reviewDto) {
//        //To check if book with the title exists in the book database
//        if(!bookEntityRepository.existsByTitle(reviewDto.getTitle())){
//            throw new ApiException(String.format("Book with this title: %s does not exist", reviewDto.getTitle()));
//        }
//
//        //To check if user/customer is registered in the database
//        if(!customerRepository.existsByUsername(reviewDto.getUsername())){
//            throw new ApiException(String.format("User with username: %s does not exist", reviewDto.getUsername()));
//        }
//
//        //new review
//        Review review = Review.builder()
//                .username(reviewDto.getUsername())
//                .title(reviewDto.getTitle())
//                .likes(likeFunction(reviewDto.getLikes()))
//                .starRating(rateFunction(reviewDto.getStarRating()))
//                .build();
//
//        //To save review to database
//        reviewRepository.save(review);
//
//        //To increase total number of likes if likes was included in the 'payload'
//        if(!ObjectUtils.isEmpty(reviewDto.getTitle())){
//            review.setTotalLikes(((long) reviewRepository.findByTitle(review.getTitle()).get()
//                    .stream().mapToInt(Review::getLikes).sum()));
//        }
//
//        //To set average rating if rating was included in the 'payload'
//        if(!ObjectUtils.isEmpty(reviewDto.getStarRating())){
//            review.setAverageRating((double)reviewRepository.findByTitle(review.getTitle()).get()
//                    .stream().mapToInt(Review::getStarRating).sum()
//                    /
//                    reviewRepository.findByTitle(review.getTitle()).get().size());
//        }
//
//        //save totoal likes and star-rating to repository
//        reviewRepository.save(review);
//
//        return new ResponseEntity<>("Thanks for your review", HttpStatus.ACCEPTED);
//    }
}
