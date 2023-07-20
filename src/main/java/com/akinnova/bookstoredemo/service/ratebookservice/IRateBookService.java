package com.akinnova.bookstoredemo.service.ratebookservice;

import com.akinnova.bookstoredemo.dto.ratedto.RateDto;
import com.akinnova.bookstoredemo.entity.RateBook;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRateBookService {
    ResponseEntity<?> rateBook(RateDto rateDto);
    ResponsePojo<RateBook> titleRates(String title);
    ResponsePojo<List<RateBook>> allRates();
}
