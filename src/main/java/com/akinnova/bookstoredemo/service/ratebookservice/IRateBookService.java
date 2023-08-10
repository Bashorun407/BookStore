package com.akinnova.bookstoredemo.service.ratebookservice;

import com.akinnova.bookstoredemo.dto.ratedto.RateDto;
import org.springframework.http.ResponseEntity;


public interface IRateBookService {
    ResponseEntity<?> rateBook(RateDto rateDto);
    ResponseEntity<?> titleRates(String title);
    ResponseEntity<?> allRates(int pageNum, int pageSize);
}
