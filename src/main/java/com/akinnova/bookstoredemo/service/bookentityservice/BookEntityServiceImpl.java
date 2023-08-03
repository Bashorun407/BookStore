package com.akinnova.bookstoredemo.service.bookentityservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.bookentitydto.BookUpdateDto;
import com.akinnova.bookstoredemo.entity.QBookEntity;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.dto.bookentitydto.BookEntityDto;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.response.ResponseUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
public class BookEntityServiceImpl implements IBookEntityService {
    @Autowired
    private EntityManager entityManager;
    private final BookEntityRepository bookStoreRepository;

    //Class Constructor
    public BookEntityServiceImpl(BookEntityRepository bookStoreRepository) {
        this.bookStoreRepository = bookStoreRepository;
    }

    //1) Creating a method for creating a book object
    public ResponsePojo<BookEntity> createBook(BookEntityDto bookStoreDto){

        BookEntity bookStore = BookEntity.builder()
                .imageAddress(bookStoreDto.getImageAddress())
                .title(bookStoreDto.getTitle())
                .author(bookStoreDto.getAuthor())
                .genre(bookStoreDto.getGenre())
                .summary(bookStoreDto.getSummary())
                .serialNumber(ResponseUtils.generateBookSerialNumber(10, bookStoreDto.getTitle()))
                .edition(bookStoreDto.getEdition())
                .volume(bookStoreDto.getVolume())
                .quantity(bookStoreDto.getQuantity())
                .price(bookStoreDto.getPrice())
                .supplyDate(LocalDateTime.now())
                .deleteStatus(false)
                .build();

        //saving the new object into the repository
        bookStoreRepository.save(bookStore);

        ResponsePojo<BookEntity> responsePojo = new ResponsePojo<>();
        responsePojo.setData(bookStore);
        responsePojo.setMessage("Book Created Successfully!");

        return responsePojo;
    }

    //2) Method to find all books
    public ResponseEntity<?> findAllBooks(int pageNum, int pageSize){
        //Only books that have not been deleted will be collected
        List<BookEntity> allBooks = bookStoreRepository.findAll()
                .stream().skip(pageNum - 1).limit(pageSize).filter((x)-> x.getDeleteStatus().equals(false)).collect(Collectors.toList());

        //If book entity list is empty...
        if(allBooks.isEmpty()) {
            return new ResponseEntity<>("Books not found", HttpStatus.NOT_FOUND);
        }

        //return new ResponseEntity<>(allBooks, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Book-Page-Number", String.valueOf(pageNum))
                .header("Book-Page-Size", String.valueOf(pageSize))
                .header("Book-Total-Count", String.valueOf(allBooks.size()))
                .body(allBooks);
    }

    //3) Methods to find books based on different parameters
    //defining some methods to query the repository
    public ResponseEntity<?> findBookByAuthor(String author, int pageNum, int pageSize){
;
        List<BookEntity> bookEntityList = bookStoreRepository.findBookByAuthor(author).get().stream()
                .skip(pageNum - 1).limit(pageSize)
                .filter(x-> x.getDeleteStatus().equals(false)).collect(Collectors.toList());

        //If book entity list is empty...
        if(bookEntityList.isEmpty()){
            return new ResponseEntity<>(String.format("Books by author: %s not found", author), HttpStatus.NOT_FOUND);
        }

        //return new ResponseEntity<>(bookEntityList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Book-Page-Number", String.valueOf(pageNum))
                .header("Book-Page-Size", String.valueOf(pageSize))
                .header("Book-Total-Count", String.valueOf(bookEntityList.size()))
                .body(bookEntityList);
    }

    //4) Method to find book by title
    public ResponseEntity<?> findBookByTitle(String title){
        List<BookEntity> bookEntityList = bookStoreRepository.findBookByTitle(title).stream()
                .filter(x-> x.getDeleteStatus().equals(false)).collect(Collectors.toList());

        //If book entity list is empty...
        if(bookEntityList.isEmpty()){
            return new ResponseEntity<>(String.format("Books by title: %s not found", title), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(bookEntityList, HttpStatus.FOUND);
    }

    //5) Method to find book by genre
    public ResponseEntity<?> findBooksByGenre(String genre, int pageNum, int pageSize){
        //Return only books that have not been deleted
        List<BookEntity> books = bookStoreRepository.findBooksByGenre(genre).get()
                .stream().skip(pageNum - 1).limit(pageSize).filter(x-> x.getDeleteStatus().equals(false)).collect(Collectors.toList());
        //If books is null
        if (books.isEmpty()){
            return new ResponseEntity<>(String.format("Books by title: %s not found", genre), HttpStatus.NOT_FOUND);
        }

        //return new ResponseEntity<>(books, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Book-Page-Number", String.valueOf(pageNum))
                .header("Book-Page-Size", String.valueOf(pageSize))
                .header("Book-Total-Count", String.valueOf(books.size()))
                .body(books);
    }

    //6) Method to find book by serial number
    public ResponseEntity<?> findBookBySerialNumber(String serialNumber){
        //Retrieve only books that are active
        Optional<BookEntity> book = bookStoreRepository.findBookBySerialNumber(serialNumber)
                .filter(x-> x.getDeleteStatus().equals(false));

        //If books is null
        if (book.isEmpty()){
         return new ResponseEntity<>(String.format("Book by serial number: %s not found", serialNumber),
                 HttpStatus.NOT_FOUND);
        }

       return new ResponseEntity<>(book, HttpStatus.FOUND);
    }

    //7) Method to update book content
    public ResponsePojo<BookEntity> updateBookContent(BookUpdateDto bookUpdateDto){


        //To find book in the repository i.e. books that have not been deleted
        Optional<BookEntity> findBook = bookStoreRepository.findBookBySerialNumber(bookUpdateDto.getSerialNumber())
                .filter(x-> x.getDeleteStatus().equals(false));

        //throw exception if book is not found
        if(findBook.isEmpty()){
            ResponsePojo<BookEntity> responsePojo = new ResponsePojo<>();
            responsePojo.setSuccess(false);
            responsePojo.setStatusCode(ResponseUtils.NOT_FOUND);
            responsePojo.setMessage(String.format(ResponseUtils.NOT_FOUND_MESSAGE, bookUpdateDto.getSerialNumber()));

            return responsePojo;
        }

        //A temporary container which represents fetched book
        BookEntity bookToUpdate = findBook.get();
        bookToUpdate.setImageAddress(bookUpdateDto.getImageAddress());
        bookToUpdate.setSummary(bookUpdateDto.getSummary());
        bookToUpdate.setEdition(bookUpdateDto.getEdition());
        bookToUpdate.setQuantity(bookUpdateDto.getQuantity());
        bookToUpdate.setPrice(bookUpdateDto.getPrice());
        bookToUpdate.setDeleteStatus(false);

        //Saving the updated book in the repository
        bookStoreRepository.save(bookToUpdate);

        ResponsePojo<BookEntity> responsePojo = new ResponsePojo<>();
        responsePojo.setData(bookToUpdate);
        responsePojo.setMessage("Book updated");

        return responsePojo;
    }

    //8) Method to delete book
    public ResponseEntity<?> deleteBook(String serialNumber){
        //Optional search for book with specified serial number
        Optional<BookEntity> book = bookStoreRepository.findBookBySerialNumber(serialNumber)
                .filter(x-> x.getDeleteStatus().equals(false));

        //if book is not found, throw an exception
        if(book.isEmpty()){
            return new ResponseEntity<>("Book by " + serialNumber + " is not found.", HttpStatus.NOT_FOUND);
        }

        //Only book delete status will be changed
        book.get().setDeleteStatus(true);

        //Save changes to book status
        bookStoreRepository.save(book.get());

        return new ResponseEntity<>("Book by " + serialNumber + " has been deleted", HttpStatus.ACCEPTED);

    }

    //9) Method to search book by multiple parameters
    @Override
    public ResponseEntity<?> searchBook(String title, String author, String genre,
                                                     String serialNumber, Pageable pageable) {

        QBookEntity qBookEntity = QBookEntity.bookEntity;
        BooleanBuilder predicate = new BooleanBuilder();

        if(StringUtils.hasText(title))
            predicate.and(qBookEntity.title.likeIgnoreCase("%" + title + "%"));

        if(StringUtils.hasText(author))
            predicate.and(qBookEntity.author.likeIgnoreCase("%" + author + "%"));

        if(StringUtils.hasText(genre))
            predicate.and(qBookEntity.genre.likeIgnoreCase("%" + genre + "%"));

        if(StringUtils.hasText(serialNumber))
            predicate.and(qBookEntity.serialNumber.eq(serialNumber));

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<BookEntity> jpaQuery = jpaQueryFactory.selectFrom(qBookEntity)
                .where(predicate.and(qBookEntity.deleteStatus.eq(false)))
                .orderBy(qBookEntity.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Page<BookEntity> bookEntityPage = new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());

        //If book-search does not return any value..notify the searcher
        if(bookEntityPage.isEmpty()){
          return new ResponseEntity<>("Your search does not match any item", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(bookEntityPage, HttpStatus.FOUND);
    }
}
