package com.akinnova.bookstoredemo.Exception;

public class ApiException extends RuntimeException{
    public ApiException(String message){
        super(message);
    }
}
