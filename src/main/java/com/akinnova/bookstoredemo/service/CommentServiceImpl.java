package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.CommentDto;
import com.akinnova.bookstoredemo.entity.Comment;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CommentRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookEntityRepository bookEntityRepository;

    //1) Method to comment
    @Override
    public ResponseEntity<?> commentAboutBook(CommentDto commentDto) {
        //To check that customer is registered and is in the customer database
        if(!customerRepository.existsByUsername(commentDto.getUsername())){
            throw new ApiException(String.format("User with this username: %s is not allowed. Ensure to register first.",
                    commentDto.getUsername()));
        }

        //To check that book that will be comment on still exists
        if(!bookEntityRepository.existsByTitle(commentDto.getTitle())){
            throw new ApiException(String.format("Book with this title: %s does not exist.",
                    commentDto.getTitle()));
        }
        Comment comment = Comment.builder()
                .title(commentDto.getTitle())
                .comment(commentDto.getComment())
                .username(commentDto.getUsername())
                .commentTime(LocalDateTime.now())
                .build();

        //Saving new comment into the database
        commentRepository.save(comment);
        return new ResponseEntity<>("Comment sent", HttpStatus.ACCEPTED);

    }

    //2) Method to view comments by a username
    @Override
    public ResponsePojo<List<Comment>> commentByUsername(String username) {
        //To retrieve comments by a user
        List<Comment> commentList = commentRepository.findByUsername(username).get();

        ResponsePojo<List<Comment>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format("Comments by %s found.", username));
        responsePojo.setData(commentList);
        return responsePojo;
    }

    //2b) Method to view comments on a book title
    @Override
    public ResponsePojo<List<Comment>> commentByTitle(String title) {
        //To retrieve comments for a book through title
        List<Comment> commentList = commentRepository.findByTitle(title).get();

        ResponsePojo<List<Comment>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage(String.format("Comments by %s found.", title));
        responsePojo.setData(commentList);
        return responsePojo;
    }

    //3) Method to view all comments
    @Override
    public ResponsePojo<List<Comment>> allComments() {
        //To retrieve all comments
        List<Comment> allComments = commentRepository.findAll();

        ResponsePojo<List<Comment>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("All comments found");
        responsePojo.setData(allComments);
        return responsePojo;
    }
}
