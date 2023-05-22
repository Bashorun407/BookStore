package com.akinnova.bookstoredemo.ResponsePojo;

import lombok.Data;

@Data
public class BookResponsePojo<T> {

    String message;
    int statusCode = 200;
    T data;
    boolean success = true;

}
