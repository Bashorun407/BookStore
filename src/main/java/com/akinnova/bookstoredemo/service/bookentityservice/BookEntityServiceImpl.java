package com.akinnova.bookstoredemo.service.bookentityservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.bookentitydto.BookResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookEntityServiceImpl implements IBookEntityService {

    private final EntityManager entityManager;
    private final BookEntityRepository bookStoreRepository;

    //Class Constructor
    public BookEntityServiceImpl(EntityManager entityManager, BookEntityRepository bookStoreRepository) {
        this.entityManager = entityManager;
        this.bookStoreRepository = bookStoreRepository;
    }

    //1) Creating a method for creating a book object
    public ResponsePojo<BookResponseDto> createBook(BookEntityDto bookStoreDto){

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
        BookEntity savedBook = bookStoreRepository.save(bookStore);
        BookResponseDto responseDto = BookResponseDto.builder()
                .imageAddress(savedBook.getImageAddress())
                .title(savedBook.getTitle())
                .author(savedBook.getAuthor())
                .price(savedBook.getPrice())
                .build();

        ResponsePojo<BookResponseDto> responsePojo = new ResponsePojo<>();
        responsePojo.setData(responseDto);
        responsePojo.setMessage("Book Created Successfully!");

        return responsePojo;
    }

    //2) Method to find all books
    public ResponseEntity<?> findAllBooks(int pageNum, int pageSize){
        //Only books that have not been deleted will be collected
        List<BookEntity> allBooks = bookStoreRepository.findAll()
                .stream().skip(pageNum - 1).limit(pageSize).filter((x)-> x.getDeleteStatus().equals(false)).toList();

        List<BookResponseDto> responseDtoList = new ArrayList<>();

        //Returning dto
        allBooks.stream().map(
                bookEntity -> BookResponseDto.builder()
                        .imageAddress(bookEntity.getImageAddress())
                        .title(bookEntity.getTitle())
                        .author(bookEntity.getAuthor())
                        .summary(bookEntity.getSummary())
                        .price(bookEntity.getPrice())
                        .build()
        ).forEach(responseDtoList::add);

        //If book entity list is empty...
        if(allBooks.isEmpty()) {
            return new ResponseEntity<>("Books not found", HttpStatus.NOT_FOUND);
        }


        //return new ResponseEntity<>(allBooks, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Book-Page-Number", String.valueOf(pageNum))
                .header("Book-Page-Size", String.valueOf(pageSize))
                .header("Book-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    //3) Methods to find books based on different parameters
    //defining some methods to query the repository
    public ResponseEntity<?> findBookByAuthor(String author, int pageNum, int pageSize){

        List<BookEntity> bookEntityList = bookStoreRepository.findBookByAuthor(author)
                .orElseThrow(()-> new ApiException(String.format("Books by author: %s not found", author)));
        List<BookResponseDto> responseDtoList = new ArrayList<>();
                //.filter(x-> x.getDeleteStatus().equals(false)).collect(Collectors.toList());

        bookEntityList.stream().skip(pageNum - 1).limit(pageSize).filter(x-> !x.getDeleteStatus())
                .map(
                        bookEntity -> BookResponseDto.builder()
                                .imageAddress(bookEntity.getImageAddress())
                                .title(bookEntity.getTitle())
                                .author(bookEntity.getAuthor())
                                .summary(bookEntity.getSummary())
                                .price(bookEntity.getPrice())
                                .build()
                ).forEach(responseDtoList::add);

        //return new ResponseEntity<>(bookEntityList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Book-Page-Number", String.valueOf(pageNum))
                .header("Book-Page-Size", String.valueOf(pageSize))
                .header("Book-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    //4) Method to find book by title
    public ResponseEntity<?> findBookByTitle(String title){
         BookEntity bookEntity = bookStoreRepository.findBookByTitle(title).filter(x-> !x.getDeleteStatus())
                .orElseThrow(()-> new ApiException(String.format("Books by title: %s not found", title)));
           BookResponseDto responseDto = BookResponseDto.builder()
                   .imageAddress(bookEntity.getImageAddress())
                   .title(bookEntity.getTitle())
                   .author(bookEntity.getAuthor())
                   .summary(bookEntity.getSummary())
                   .price(bookEntity.getPrice())
                   .build();

        return ResponseEntity.ok(responseDto);
    }

    //5) Method to find book by genre
    public ResponseEntity<?> findBooksByGenre(String genre, int pageNum, int pageSize){
        //Return only books that have not been deleted
        List<BookEntity> bookEntityList = bookStoreRepository.findBooksByGenre(genre)
                .orElseThrow(()-> new ApiException(String.format("Books by title: %s not found", genre)));
        List<BookResponseDto> responseDtoList = new ArrayList<>();

        bookEntityList.stream().skip(pageNum - 1).limit(pageSize).filter(x-> !x.getDeleteStatus())
                .map(
                        bookEntity -> BookResponseDto.builder()
                                .imageAddress(bookEntity.getImageAddress())
                                .title(bookEntity.getTitle())
                                .author(bookEntity.getAuthor())
                                .summary(bookEntity.getSummary())
                                .price(bookEntity.getPrice())
                                .build()
                ).forEach(responseDtoList::add);

        //return new ResponseEntity<>(books, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Book-Page-Number", String.valueOf(pageNum))
                .header("Book-Page-Size", String.valueOf(pageSize))
                .header("Book-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    //6) Method to find book by serial number
    public ResponseEntity<?> findBookBySerialNumber(String serialNumber){
        //Retrieve only books that are active
        BookEntity book = bookStoreRepository.findBookBySerialNumber(serialNumber)
                .filter(x-> x.getDeleteStatus().equals(false))
                .orElseThrow(()-> new ApiException(String.format("Book by serial number: %s not available", serialNumber)));

        //Response dto
        BookResponseDto responseDto = BookResponseDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .summary(book.getSummary())
                .price(book.getPrice())
                .build();

       return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    //7) Method to update book content
    public ResponseEntity<BookResponseDto> updateBookContent(BookUpdateDto bookUpdateDto){


        //To find book in the repository i.e. books that have not been deleted
        BookEntity bookToUpdate = bookStoreRepository.findBookBySerialNumber(bookUpdateDto.getSerialNumber())
                .orElseThrow(()-> new ApiException(String.format("Books by serial number: %s not found",
                        bookUpdateDto.getSerialNumber())));


        bookToUpdate.setImageAddress(bookUpdateDto.getImageAddress());
        bookToUpdate.setSummary(bookUpdateDto.getSummary());
        bookToUpdate.setEdition(bookUpdateDto.getEdition());
        bookToUpdate.setQuantity(bookUpdateDto.getQuantity());
        bookToUpdate.setPrice(bookUpdateDto.getPrice());
        bookToUpdate.setDeleteStatus(false);

        //Saving the updated book in the repository
        BookEntity savedBook = bookStoreRepository.save(bookToUpdate);
        BookResponseDto responseDto = BookResponseDto.builder()
                .imageAddress(savedBook.getImageAddress())
                .title(savedBook.getTitle())
                .author(savedBook.getAuthor())
                .summary(savedBook.getSummary())
                .price(savedBook.getPrice())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    //8) Method to delete book
    public ResponseEntity<?> deleteBook(String serialNumber){
        //Optional search for book with specified serial number
        BookEntity book = bookStoreRepository.findBookBySerialNumber(serialNumber)
                .filter(x-> x.getDeleteStatus().equals(Boolean.FALSE))
                .orElseThrow(()-> new ApiException("Book by serial number: " + serialNumber + " is not found."));



        //Only book delete status will be changed
        book.setDeleteStatus(true);

        //Save changes to book status
        bookStoreRepository.save(book);

        return new ResponseEntity<>("Book with serial number: " + serialNumber + " has been deleted", HttpStatus.ACCEPTED);

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

        //If book-search does not return any value and notify the searcher
        if(bookEntityPage.isEmpty()){
          return new ResponseEntity<>("Your search does not match any item", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(bookEntityPage, HttpStatus.FOUND);
    }
}
