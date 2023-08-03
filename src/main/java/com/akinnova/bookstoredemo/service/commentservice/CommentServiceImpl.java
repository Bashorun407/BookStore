package com.akinnova.bookstoredemo.service.commentservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.commentdto.CommentDeleteDto;
import com.akinnova.bookstoredemo.dto.commentdto.CommentDto;
import com.akinnova.bookstoredemo.entity.Comment;
import com.akinnova.bookstoredemo.entity.QComment;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CommentRepository;
import com.akinnova.bookstoredemo.repository.CustomerRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private EntityManager entityManager;
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
    public ResponsePojo<Comment> commentAboutBook(CommentDto commentDto) {

        Comment comment = Comment.builder()
                .title(commentDto.getTitle())
                .comment(commentDto.getComment())
                .username(commentDto.getUsername())
                .commentTime(LocalDateTime.now())
                .build();

        ResponsePojo<Comment> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Comment sent");
        responsePojo.setData(comment);

        //Saving new comment into the database
        commentRepository.save(comment);
        return responsePojo;

    }

    //2) Method to view comments by a username
    @Override
    public ResponseEntity<?> commentByUsername(String username, int pageNum, int pageSize) {
        //To retrieve comments by a user
        List<Comment> commentList = commentRepository.findByUsername(username).get().stream().skip(pageNum - 1)
                .limit(pageSize).toList();

        if(commentList.isEmpty())
            return new ResponseEntity<>(String.format("Comments by %s not found.", username), HttpStatus.NOT_FOUND);

        //return new ResponseEntity<>(commentList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Comment-Page-Number", String.valueOf(pageNum))
                .header("Comment-Page-Size", String.valueOf(pageSize))
                .header("Comment-Total-Count", String.valueOf(commentList.size()))
                .body(commentList);

    }

    //2b) Method to view comments on a book title
    @Override
    public ResponseEntity<?> commentByTitle(String title, int pageNum, int pageSize) {
        //To retrieve comments for a book through title
        List<Comment> commentList = commentRepository.findByTitle(title).get().stream().skip(pageNum - 1)
                .limit(pageSize).collect(Collectors.toList());

        if(commentList.isEmpty())
            return new ResponseEntity<>(String.format("Comments on book titled: %s not found.", title), HttpStatus.NOT_FOUND);

        //return new ResponseEntity<>(commentList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Comment-Page-Number", String.valueOf(pageNum))
                .header("Comment-Page-Size", String.valueOf(pageSize))
                .header("Comment-Total-Count", String.valueOf(commentList.size()))
                .body(commentList);

    }

    //3) Method to view all comments
    @Override
    public ResponseEntity<?> allComments(int pageNum, int pageSize) {
        //To retrieve all comments
        List<Comment> allComments = commentRepository.findAll().stream().skip(pageNum - 1).limit(pageSize)
                .collect(Collectors.toList());

        if(allComments.isEmpty())
            return new ResponseEntity<>("No comments found", HttpStatus.NOT_FOUND);

        //return new ResponseEntity<>(allComments, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Comment-Page-Number", String.valueOf(pageNum))
                .header("Comment-Page-Size", String.valueOf(pageSize))
                .header("Comment-Total-Count", String.valueOf(allComments.size()))
                .body(allComments);
    }

    //4) Method to delete comments by username
    @Override
    public ResponseEntity<?> deleteComment(CommentDeleteDto commentDeleteDto){
        //To check that customer is registered and is in the customer database
        if(!customerRepository.existsByUsername(commentDeleteDto.getUsername())){
            throw new ApiException(String.format("User with this username: %s is not allowed. Ensure to register first.",
                    commentDeleteDto.getUsername()));
        }

        //To check that book that will be comment on still exists
        if(!bookEntityRepository.existsByTitle(commentDeleteDto.getTitle())) {
            throw new ApiException(String.format("Book with this title: %s does not exist.",
                    commentDeleteDto.getTitle()));
        }

        //Retrieves all comments on a book by customers/users
        Optional<List<Comment>> commentList = commentRepository.findByTitle(commentDeleteDto.getTitle());
        commentList.orElseThrow(()->new ApiException(String.format("There are no comments on this book titled: %s.", commentDeleteDto.getTitle())));

        List<Comment> comments = commentList.get();
        Comment comment = comments.stream().findFirst().get();
        commentRepository.delete(comment);

        return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
    }

    //5) Method to search for comments using title or username
    @Override
    public ResponseEntity<?> searchComment(String title, String username, Pageable pageable) {

        QComment qComment = QComment.comment1;
        BooleanBuilder predicate = new BooleanBuilder();

        if(StringUtils.hasText(title))
            predicate.and(qComment.title.likeIgnoreCase("%" + title + "%"));

        if(StringUtils.hasText(username))
            predicate.and(qComment.username.likeIgnoreCase("%" + username + "%"));

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<Comment> jpaQuery = jpaQueryFactory.selectFrom(qComment)
                .where(predicate)
                .orderBy(qComment.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //Implementing Pageable response
        Page<Comment> commentPage = new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());

        if(commentPage.isEmpty())
            return new ResponseEntity<>("Your search does not match any item", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(commentPage, HttpStatus.FOUND);
    }
}
