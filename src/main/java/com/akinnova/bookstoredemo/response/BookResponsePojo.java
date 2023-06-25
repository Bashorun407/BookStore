package com.akinnova.bookstoredemo.response;

import lombok.Data;

@Data
public class BookResponsePojo<T> {

    String statusCode = "200";
    String message;
    T data;
    boolean success = true;

}
