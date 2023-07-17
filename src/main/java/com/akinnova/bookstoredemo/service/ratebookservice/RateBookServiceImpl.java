package com.akinnova.bookstoredemo.service.ratebookservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.ratedto.RateDto;
import com.akinnova.bookstoredemo.entity.RateBook;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.repository.RateBookRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import com.akinnova.bookstoredemo.service.ratebookservice.IRateBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        //If the book has been reviewed by user prior...search for review in the review repository
        List<RateBook> userRateList = rateBookRepository.findByUsername(rateDto.getUsername()).get();

        //If user has not reviewed any book prior, create a new review for user
        if(userRateList.isEmpty()){
            //new review
            RateBook rateBook = RateBook.builder()
                    .username(rateDto.getUsername())
                    .title(rateDto.getTitle())
                    .starRating(ResponseUtils.rateFunction(rateDto.getStarRating()))
                    .build();

            //To save review to database
            rateBookRepository.save(rateBook);

            //To set average rating if rating was included in the 'payload'
            rateBook.setAverageRating((double)rateBookRepository.findByTitle(rateBook.getTitle()).get()
                    .stream().mapToInt(RateBook::getStarRating).sum()
                    /
                    rateBookRepository.findByTitle(rateBook.getTitle()).get().size());

            //To save review to database
            rateBookRepository.save(rateBook);
            //return new ResponseEntity<>("Thanks for your new review.", HttpStatus.ACCEPTED);
        }

        //If user has reviewed a book before, retrieve the exact record and edit
        else if(!userRateList.isEmpty()) {

            RateBook rateUpdate = rateBookRepository.findByTitle(rateDto.getTitle()).get().stream()
                    .filter(x-> x.getUsername().equals(rateDto.getUsername()))
                    .findFirst()
                    .map(rates -> {
                        rates.setStarRating(ResponseUtils.rateFunction(rateDto.getStarRating()));
                        rates.setAverageRating(
                                (double) rateBookRepository.findByTitle(rateDto.getTitle()).get().stream()
                                        .mapToInt(RateBook::getStarRating).sum()
                                        /
                                        rateBookRepository.findByTitle(rateDto.getTitle()).get().size()
                        );

                        //returns review to its caller
                        return rates;
                    }).get();

            //Save update into the database
            rateBookRepository.save(rateUpdate);
       }

        return new ResponseEntity<>("Thanks for your review", HttpStatus.ACCEPTED);
    }

    //2) Method to retrieve reviews on specific book title
    @Override
    public ResponsePojo<List<RateBook>> titleRates(String title) {

        //To check if book with the specified title exists in the book database
        if(!bookEntityRepository.existsByTitle(title)){
            throw new ApiException(String.format("Book by this title: %s does not exist", title));
        }

        //To retrieve all reviews for a book by title
        List<RateBook> rateBookList = rateBookRepository.findByTitle(title).get();
        ResponsePojo<List<RateBook>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format("Reviews for %s: ", title));
        responsePojo.setData(rateBookList);
        return responsePojo;
    }

    //3) Method to retrieve all reviews in the database
    @Override
    public ResponsePojo<List<RateBook>> allRates() {
        //To retrieve all reviews in the review database
        List<RateBook> rateBookList = rateBookRepository.findAll();
        ResponsePojo<List<RateBook>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("All reviews: ");
        responsePojo.setData(rateBookList);
        return responsePojo;
    }
}
