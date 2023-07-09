package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.CommentDto;
import com.akinnova.bookstoredemo.entity.Comment;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CommentRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final CustomerRepository customerRepository;
    private final BookEntityRepository bookEntityRepository;

    //Class Constructor
    public CommentServiceImpl(CommentRepository commentRepository, CustomerRepository customerRepository,
                              BookEntityRepository bookEntityRepository) {
        this.commentRepository = commentRepository;
        this.customerRepository = customerRepository;
        this.bookEntityRepository = bookEntityRepository;
    }

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

    //4) Method to delete comments by username
    @Override
    public ResponseEntity<?> deleteComment(CommentDto commentDto){
        //To check that customer is registered and is in the customer database
        if(!customerRepository.existsByUsername(commentDto.getUsername())){
            throw new ApiException(String.format("User with this username: %s is not allowed. Ensure to register first.",
                    commentDto.getUsername()));
        }

        //To check that book that will be comment on still exists
        if(!bookEntityRepository.existsByTitle(commentDto.getTitle())) {
            throw new ApiException(String.format("Book with this title: %s does not exist.",
                    commentDto.getTitle()));
        }

        //Retrieves all comments on a book by customers/users
        Optional<List<Comment>> commentList = commentRepository.findByTitle(commentDto.getTitle());
        commentList.orElseThrow(()->new ApiException(String.format("There are no comments on this book titled: %s.", commentDto.getTitle())));

        //To get particular comment by user using title and username of user
//        Long commentToDelete = commentList.get().stream()
//                .filter(x -> x.getTitle() == commentDto.getTitle() && x.getUsername() == x.getUsername())
//                .findFirst()
//                .orElseThrow(()-> new ApiException("Book Not found"))
//                .getId();
//                commentRepository.deleteById(commentToDelete);

        List<Comment> comments = commentList.get();
        Comment comment = comments.stream().findFirst().get();
        commentRepository.delete(comment);

        return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
    }
}
