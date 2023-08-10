package com.akinnova.bookstoredemo.service.commentservice;

import com.akinnova.bookstoredemo.dto.commentdto.CommentDeleteDto;
import com.akinnova.bookstoredemo.dto.commentdto.CommentDto;
import com.akinnova.bookstoredemo.entity.Comment;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface ICommentService {
    ResponsePojo<Comment> commentAboutBook(CommentDto commentDto);
    ResponseEntity<?> commentByUsername(String username, int pageNum, int pageSize);
    ResponseEntity<?> commentByTitle(String title, int pageNum, int pageSize);
    ResponseEntity<?> allComments(int pageNum, int pageSize);
    ResponseEntity<?> deleteComment(CommentDeleteDto commentDeleteDto);
    ResponseEntity<?> searchComment(String title, String username, Pageable pageable);

}
