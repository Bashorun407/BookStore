package com.akinnova.bookstoredemo.service.bookentityservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
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

        //Checks if Book with the same title and volume already exists in the database
        if(bookStoreRepository.existsByTitle(bookStoreDto.getTitle()))
            throw new ApiException(String.format("Book with title: %s and volume: %d already exist", bookStoreDto.getTitle(), bookStoreDto.getVolume()));

        BookEntity bookStore = BookEntity.builder()
                .title(bookStoreDto.getTitle())
                .author(bookStoreDto.getAuthor())
                .genre(bookStoreDto.getGenre())
                .summary(bookStoreDto.getSummary())
                .serialNumber(ResponseUtils.generateBookSerialNumber(10, bookStoreDto.getTitle()))
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
    public ResponsePojo<List<BookEntity>> findAllBooks(){
        //Only books that have not been deleted will be collected
        List<BookEntity> allBooks = bookStoreRepository.findAll()
                .stream().filter((x)-> x.getDeleteStatus().equals(false)).collect(Collectors.toList());

        ResponsePojo<List<BookEntity>> responsePojo = new ResponsePojo<>();
        responsePojo.setData(allBooks);
        responsePojo.setSuccess(true);
        responsePojo.setMessage("Books fetch complete");

        return responsePojo;
    }

    //3) Methods to find books based on different parameters
    //defining some methods to query the repository
    public ResponsePojo<List<BookEntity>> findBookByAuthor(String author){

        Optional<List<BookEntity>> bookList = bookStoreRepository.findBookByAuthor(author);
        bookList.orElseThrow(()->new ApiException(String.format("Books with author %s not found", author)));

        //Collecting only books that have not been deleted by filtering Optional List
        List<BookEntity> booksToReturn = bookList.get().stream()
                .filter(x-> x.getDeleteStatus().equals(false)).collect(Collectors.toList());

        ResponsePojo<List<BookEntity>> responsePojo = new ResponsePojo<>();
        responsePojo.setData(booksToReturn);
        responsePojo.setMessage(String.format("Book(s) by Author: %s found.", author));

        return responsePojo;
    }

    //4) Method to find book by title
    public ResponsePojo<BookEntity> findBookByTitle(String title){
        Optional<BookEntity> bookEntityOptional = bookStoreRepository.findBookByTitle(title);
        bookEntityOptional.orElseThrow(()-> new ApiException(String.format("Book titled %s not found.", title)));

        //Collecting only books that have not been deleted by filtering Optional List
//        List<BookEntity> booksToReturn = bookList.get().stream()
//                .filter(x-> x.getDeleteStatus().equals(false)).collect(Collectors.toList());

        ResponsePojo<BookEntity> responsePojo = new ResponsePojo<>();
        responsePojo.setData(bookEntityOptional.get());
        responsePojo.setMessage("Books by title found");

        return responsePojo;
    }

    //5) Method to find book by genre
    public ResponsePojo<List<BookEntity>> findBooksByGenre(String genre){
        Optional<List<BookEntity>> books = bookStoreRepository.findBooksByGenre(genre);
        books.orElseThrow(()-> new ApiException(String.format("Book by %s not found", genre)));

        ResponsePojo<List<BookEntity>> responsePojo = new ResponsePojo<>();
        responsePojo.setData(books.get());
        responsePojo.setMessage("books by genre found.");

        return responsePojo;
    }

    //6) Method to find book by serial number
    public ResponsePojo<BookEntity> findBookBySerialNumber(String serialNumber){
        Optional<BookEntity> book = bookStoreRepository.findBookBySerialNumber(serialNumber);
        book.orElseThrow(()-> new ApiException(String.format("Book by %s not found", serialNumber)));

        ResponsePojo<BookEntity> responsePojo = new ResponsePojo<>();
        responsePojo.setData(book.get());
        responsePojo.setMessage("Book by serial number found.");

        return responsePojo;
    }

    //7) Method to update book content
    public ResponsePojo<BookEntity> updateBookContent(BookEntityDto bookStoreDto){

        //To find book in the repository i.e. books that have not been deleted
        Optional<BookEntity> findBook = bookStoreRepository.findById(bookStoreDto.getId()).filter(x-> x.getDeleteStatus().equals(false));

        //throw exception if book is not found
        findBook.orElseThrow(()-> new ApiException(String.format("Book with Id: %d not found", bookStoreDto.getId())));

        //A temporary container which represents fetched book
        BookEntity bookToUpdate = findBook.get();

        bookToUpdate.setTitle(bookStoreDto.getTitle());
        bookToUpdate.setAuthor(bookStoreDto.getAuthor());
        bookToUpdate.setSummary(bookStoreDto.getSummary());
        bookToUpdate.setVolume(bookStoreDto.getVolume());
        bookToUpdate.setQuantity(bookStoreDto.getQuantity());
        bookToUpdate.setPrice(bookStoreDto.getPrice());

        //Saving the updated book in the repository
        bookStoreRepository.save(bookToUpdate);

        ResponsePojo<BookEntity> responsePojo = new ResponsePojo<>();
        responsePojo.setData(bookToUpdate);
        responsePojo.setMessage("Book updated");

        return responsePojo;
    }

    //8) Method to delete book
    public ResponsePojo<String> deleteBook(Long id){

        Optional<BookEntity> book = bookStoreRepository.findById(id);
        //To check if book has been deleted
        if (book.get().getDeleteStatus().equals(true))
            throw new ApiException("This Book has already been deleted");

        //if book is not found, throw an exception
        book.orElseThrow(()-> new ApiException(String.format("Book by %d not found")));

        //Only book delete status will be changed
        book.get().setDeleteStatus(true);

        //Save changes to book status
        bookStoreRepository.save(book.get());

        ResponsePojo<String> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format("Book by %d has been deleted", id));

        return responsePojo;

    }

    //9) Method to search book by multiple parameters
    @Override
    public ResponsePojo<Page<BookEntity>> searchBook(String title, String author, String genre,
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

        ResponsePojo<Page<BookEntity>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("Book Items: ");
        responsePojo.setData(bookEntityPage);
        return responsePojo;
    }
}
