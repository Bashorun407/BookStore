package com.akinnova.bookstoredemo.service.commentservice;

import com.akinnova.bookstoredemo.dto.commentdto.CommentDeleteDto;
import com.akinnova.bookstoredemo.dto.commentdto.CommentDto;
import com.akinnova.bookstoredemo.entity.Comment;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICommentService {
    ResponsePojo<Comment> commentAboutBook(CommentDto commentDto);
    ResponseEntity<?> commentByUsername(String username);
    ResponseEntity<?> commentByTitle(String title);
    ResponseEntity<?> allComments();
    ResponseEntity<?> deleteComment(CommentDeleteDto commentDeleteDto);
    ResponseEntity<?> searchComment(String title, String username, Pageable pageable);

}
