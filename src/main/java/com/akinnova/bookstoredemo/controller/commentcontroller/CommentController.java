package com.akinnova.bookstoredemo.controller.commentcontroller;

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
    public ResponseEntity<?> commentAboutBook(@RequestBody CommentDto commentDto) {
        return commentService.commentAboutBook(commentDto);
    }

    //2) Method to view comments by a username
    @GetMapping("/comment/{username}")
    public ResponsePojo<List<Comment>> commentByUsername(@PathVariable(name = "username") String username) {
        return commentService.commentByUsername(username);
    }

    //2b) Method to view comments on a book title
    @GetMapping("/auth/comment/{title}")
    public ResponsePojo<List<Comment>> commentByTitle(@PathVariable(name = "title") String title) {
        return commentService.commentByTitle(title);
    }

    //3) Method to view all comments
    @GetMapping("/auth/comment")
    public ResponsePojo<List<Comment>> allComments() {
        return commentService.allComments();
    }

    //4) Method to delete comments by username
    @DeleteMapping("/auth/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDto commentDto){
        return commentService.deleteComment(commentDto);
    }

    //5) Method to search for comments using title or username
    @GetMapping("/search")
    public ResponsePojo<Page<Comment>> searchComment(@RequestParam(name = "title", required = false) String title,
                                                     @RequestParam(name = "username", required = false) String username,
                                                     Pageable pageable) {
        return commentService.searchComment(title, username, pageable);
    }
}
