package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.dto.LikeDto;
import com.akinnova.bookstoredemo.entity.LikeBook;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ILikeBookService {
    ResponseEntity<?> likeBook(LikeDto likeDto);
    ResponsePojo<List<LikeBook>> titleLikes(String title);
    ResponsePojo<List<LikeBook>> allLikes();
}
