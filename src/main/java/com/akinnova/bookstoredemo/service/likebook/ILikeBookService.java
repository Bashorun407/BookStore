package com.akinnova.bookstoredemo.service.likebook;

import com.akinnova.bookstoredemo.dto.likedto.LikeDto;
import org.springframework.http.ResponseEntity;


public interface ILikeBookService {
    ResponseEntity<?> likeBook(LikeDto likeDto);
    ResponseEntity<?> titleLikes(String title);
    ResponseEntity<?> allLikes(int pageNum, int pageSize);
}
