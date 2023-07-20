package com.akinnova.bookstoredemo.service.likebook;

import com.akinnova.bookstoredemo.dto.likedto.LikeDto;
import com.akinnova.bookstoredemo.entity.LikeBook;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ILikeBookService {
    ResponseEntity<?> likeBook(LikeDto likeDto);
    ResponsePojo<LikeBook> titleLikes(String title);
    ResponsePojo<List<LikeBook>> allLikes();
}
