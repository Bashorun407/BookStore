package com.akinnova.bookstoredemo.service.ratebookservice;

import com.akinnova.bookstoredemo.dto.ratedto.RateDto;
import com.akinnova.bookstoredemo.entity.RateBook;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRateBookService {
    ResponseEntity<?> rateBook(RateDto rateDto);
    ResponseEntity<?> titleRates(String title);
    ResponseEntity<?> allRates(int pageNum, int pageSize);
}
