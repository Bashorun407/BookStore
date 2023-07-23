package com.akinnova.bookstoredemo.controller.ratebookcontroller;

import com.akinnova.bookstoredemo.dto.ratedto.RateDto;
import com.akinnova.bookstoredemo.entity.RateBook;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.ratebookservice.RateBookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rates/auth")
public class RateBookController {

    private final RateBookServiceImpl rateBookService;

    //Class Constructor

    public RateBookController(RateBookServiceImpl rateBookService) {
        this.rateBookService = rateBookService;
    }

    //1) Method to rate book...(Rate should be between 1 and 5)
    @PostMapping("/rate")
    public ResponseEntity<?> rateBook(@RequestBody RateDto rateDto) {
        return rateBookService.rateBook(rateDto);
    }

    //2) Method to retrieve reviews on specific book title
    @GetMapping("/titleRates/{title}")
    public ResponseEntity<?> titleRates(@PathVariable(name = "title") String title) {
        return rateBookService.titleRates(title);
    }

    //3) Method to retrieve all reviews in the database
    @GetMapping("/allRates")
    public ResponseEntity<?> allRates() {
        return rateBookService.allRates();
    }
}
