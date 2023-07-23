package com.akinnova.bookstoredemo.controller.commentcontroller;

import com.akinnova.bookstoredemo.dto.commentdto.CommentDeleteDto;
import com.akinnova.bookstoredemo.dto.commentdto.CommentDto;
import com.akinnova.bookstoredemo.entity.Comment;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.commentservice.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentServiceImpl commentService;

    //1) Method to comment
    @PostMapping("/auth/comment")
    public ResponsePojo<Comment> commentAboutBook(@RequestBody CommentDto commentDto) {
        return commentService.commentAboutBook(commentDto);
    }

    //2) Method to view comments by a username
    @GetMapping("/auth/commentUser/{username}")
    public ResponseEntity<?> commentByUsername(@PathVariable(name = "username") String username) {
        return commentService.commentByUsername(username);
    }

    //2b) Method to view comments on a book title
    @GetMapping("/auth/commentTitle/{title}")
    public ResponseEntity<?> commentByTitle(@PathVariable(name = "title") String title) {
        return commentService.commentByTitle(title);
    }

    //3) Method to view all comments
    @GetMapping("/auth/comment")
    public ResponseEntity<?> allComments() {
        return commentService.allComments();
    }

    //4) Method to delete comments by username
    @DeleteMapping("/auth/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDeleteDto commentDeleteDto){
        return commentService.deleteComment(commentDeleteDto);
    }

    //5) Method to search for comments using title or username
    @GetMapping("auth/search")
    public ResponseEntity<?> searchComment(@RequestParam(name = "title", required = false) String title,
                                                     @RequestParam(name = "username", required = false) String username,
                                                     Pageable pageable) {
        return commentService.searchComment(title, username, pageable);
    }
}
