package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.ResponsePojo.BookResponsePojo;
import com.akinnova.bookstoredemo.dto.BookStoreDto;
import com.akinnova.bookstoredemo.entity.BookStore;
import com.akinnova.bookstoredemo.repository.BookStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookStoreService {

    //Autowire object of BookStore repository
    @Autowired
    private BookStoreRepository bookStoreRepository;

    //Creating a method for creating a book object
    public BookResponsePojo<BookStore> createBook(BookStoreDto bookStoreDto){

        BookStore bookStore = new BookStore();
        bookStore.setTitle(bookStoreDto.getTitle());
        bookStore.setAuthor(bookStoreDto.getAuthor());
        bookStore.setGenre(bookStoreDto.getGenre());
        bookStore.setSummary(bookStoreDto.getSummary());
        bookStore.setSerialNumber(new Date().getTime());
        bookStore.setVolume(bookStoreDto.getVolume());
        bookStore.setQuantity(bookStoreDto.getQuantity());
        bookStore.setPrice(bookStoreDto.getPrice());
        bookStore.setSupplyDate(new Date());
        bookStore.setDeleteStatus(false);

        //saving the new object into the repository
        bookStoreRepository.save(bookStore);

        BookResponsePojo<BookStore> responsePojo = new BookResponsePojo<>();
        responsePojo.setData(bookStore);
        responsePojo.setMessage("Book Created Successfully!");

        return responsePojo;
    }

    //Method to find all books
    public BookResponsePojo<List<BookStore>> findAllBooks(){
        List<BookStore> allBooks = bookStoreRepository.findAll();

        BookResponsePojo<List<BookStore>> responsePojo = new BookResponsePojo<>();
        responsePojo.setData(allBooks);
        responsePojo.setMessage("All books retrieved.");

        return responsePojo;
    }

    //Methods to find books based on different parameters
    //defining some methods to query the repository
    public BookResponsePojo<List<BookStore>> findBookByAuthor(String author){

        Optional<List<BookStore>> allBooks = bookStoreRepository.findBookByAuthor(author);
        allBooks.orElseThrow(()->new ApiException(String.format("Books with author %s not found", author)));

        BookResponsePojo<List<BookStore>> responsePojo = new BookResponsePojo<>();
        responsePojo.setData(allBooks.get());
        responsePojo.setMessage("All books retrieved.");

        return responsePojo;
    }

    //Method to find book by title
    public BookResponsePojo<BookStore> findBookByTitle(String title){
        Optional<BookStore> book = bookStoreRepository.findBookByTitle(title);
        book.orElseThrow(()-> new ApiException(String.format("Book titled %s not found.", title)));

        BookResponsePojo<BookStore> responsePojo = new BookResponsePojo<>();
        responsePojo.setData(book.get());
        responsePojo.setMessage("Books by title found");

        return responsePojo;
    }

    //Method to find book by genre
    public BookResponsePojo<List<BookStore>> findBooksByGenre(String genre){
        Optional<List<BookStore>> books = bookStoreRepository.findBooksByGenre(genre);
        books.orElseThrow(()-> new ApiException(String.format("Book by %s not found", genre)));

        BookResponsePojo<List<BookStore>> responsePojo = new BookResponsePojo<>();
        responsePojo.setData(books.get());
        responsePojo.setMessage("books by genre found.");

        return responsePojo;
    }

    //Method to find book by serial number
    public BookResponsePojo<BookStore> findBookBySerialNumber(Long serialNumber){
        Optional<BookStore> book = bookStoreRepository.findBookBySerialNumber(serialNumber);
        book.orElseThrow(()-> new ApiException(String.format("Book by %d not found", serialNumber)));

        BookResponsePojo<BookStore> responsePojo = new BookResponsePojo<>();
        responsePojo.setData(book.get());
        responsePojo.setMessage("Book by serial number found.");

        return responsePojo;
    }

    //Method to update book content
    public BookResponsePojo<BookStore> updateBookContent(BookStoreDto bookStoreDto){

        //To find book in the repository
        Optional< BookStore> findbook = bookStoreRepository.findById(bookStoreDto.getId());
        findbook.orElseThrow(()-> new ApiException(String.format("Book with Id: %d not found", bookStoreDto.getId())));

        BookStore bookStore = findbook.get(); //Assigns the found book to the BookStore object
        bookStore.setTitle(bookStoreDto.getTitle());
        bookStore.setAuthor(bookStoreDto.getAuthor());
        bookStore.setSummary(bookStoreDto.getSummary());
        bookStore.setVolume(bookStoreDto.getVolume());
        bookStore.setQuantity(bookStoreDto.getQuantity());
        bookStore.setPrice(bookStoreDto.getPrice());
        bookStore.setSupplyDate(new Date());

        //Saving the updated book in the repository
        bookStoreRepository.save(bookStore);

        BookResponsePojo<BookStore> responsePojo = new BookResponsePojo<>();
        responsePojo.setData(bookStore);
        responsePojo.setMessage("Book updated");

        return responsePojo;
    }

    //Method to delete book
    public BookResponsePojo<String> deleteBook(Long id){

        Optional<BookStore> book = bookStoreRepository.findById(id);
        book.orElseThrow(()-> new ApiException(String.format("Book by %d not found")));

        //Only book delete status will be changed
        book.get().setDeleteStatus(true);

        BookResponsePojo<String> responsePojo = new BookResponsePojo<>();
        responsePojo.setMessage(String.format("Book by %d has been deleted", id));

        return responsePojo;

    }

}
