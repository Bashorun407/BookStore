package com.akinnova.bookstoredemo.service.ratebookservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.ratedto.RateDto;
import com.akinnova.bookstoredemo.entity.RateBook;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.repository.RateBookRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RateBookServiceImpl implements IRateBookService {

    private final RateBookRepository rateBookRepository;
    private final BookEntityRepository bookEntityRepository;
    private final CustomerRepository customerRepository;

    //Class Constructor

    public RateBookServiceImpl(RateBookRepository rateBookRepository, BookEntityRepository bookEntityRepository,
                               CustomerRepository customerRepository) {
        this.rateBookRepository = rateBookRepository;
        this.bookEntityRepository = bookEntityRepository;
        this.customerRepository = customerRepository;
    }

    //1) Method to rate book...(Rate should be between 1 and 5)
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

        //If user has not reviewed any book prior, create a new review for user
        if(!rateBookRepository.existsByTitle(rateDto.getTitle())){
            //new review
            RateBook rateBook = RateBook.builder()
                    .title(rateDto.getTitle())
                    .starRating(ResponseUtils.rateFunction(rateDto.getStarRating()))
                    .rateCount((long)1)
                    .averageRating((double)rateDto.getStarRating())
                    .rateTime(LocalDateTime.now())
                    .build();

            //To save review to database
            rateBookRepository.save(rateBook);

            return new ResponseEntity<>("Thanks for your new review.", HttpStatus.ACCEPTED);
        }

        //If user has reviewed a book before, retrieve the exact record and edit
        RateBook userRate = rateBookRepository.findByTitle(rateDto.getTitle()).get();
        //obtains the current average rating
        double currentAverage = userRate.getAverageRating();
        //obtains the current rate counts (i.e. number of rates given)
        long currentCount = userRate.getRateCount();

        userRate.setStarRating(rateDto.getStarRating());
        userRate.setRateCount(userRate.getRateCount() + 1);
        //To calculate the new average: Product of former average and former count summed with new rating and all
        //...divided with new count (i.e. former count + 1
        userRate.setAverageRating(((currentAverage * currentCount) + rateDto.getStarRating())
                / (currentCount + 1));
        //Save update into the database
        rateBookRepository.save(userRate);

        return new ResponseEntity<>("Thanks for your review", HttpStatus.ACCEPTED);
    }

    //2) Method to retrieve reviews on specific book title
    @Override
    public ResponseEntity<?> titleRates(String title) {

        //To check if book with the specified title exists in the book database
        if(!bookEntityRepository.existsByTitle(title)){
            throw new ApiException(String.format("Book by this title: %s does not exist", title));
        }

        //To retrieve all reviews for a book by title
        RateBook rateBook = rateBookRepository.findByTitle(title).get();

        if(ObjectUtils.isEmpty(rateBook))
            return new ResponseEntity<>(String.format("There are no reviews for book with this title %s: yet", title),
                    HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(String.format("Rate for book title %s: %f from %d users", title, rateBook.getAverageRating(),
                rateBook.getRateCount() ), HttpStatus.FOUND);
    }

    //3) Method to retrieve all reviews in the database
    @Override
    public ResponseEntity<?> allRates() {
        //To retrieve all reviews in the review database
        List<RateBook> rateBookList = rateBookRepository.findAll();

        if(rateBookList.isEmpty())
            return new ResponseEntity<>("There are no reviews yet", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(rateBookList, HttpStatus.OK);
    }
}
