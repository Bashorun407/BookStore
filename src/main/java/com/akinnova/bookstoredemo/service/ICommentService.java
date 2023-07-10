package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.dto.CommentDto;
import com.akinnova.bookstoredemo.entity.Comment;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICommentService {
    ResponseEntity<?> commentAboutBook(CommentDto commentDto);
    ResponsePojo<List<Comment>> commentByUsername(String username);
    ResponsePojo<List<Comment>> commentByTitle(String title);
    ResponsePojo<List<Comment>> allComments();
    ResponseEntity<?> deleteComment(CommentDto commentDto);
    ResponsePojo<Page<Comment>> searchComment(String title, String username, Pageable pageable);

}
