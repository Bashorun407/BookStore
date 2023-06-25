package com.akinnova.bookstoredemo.response;

import java.util.Random;

public class ResponseUtils {
    //The following are HTTP status codes
    private static final String CONTINUE = "100";
    private static final String CONTINUE_MESSAGE = "CONTINUE";
    private static final String SWITCHING = "101";
    private static final String SWITCHING_MESSAGE = "SWITCHING";
    private static final String CHECK_POINT = "103";
    private static final String CHECK_POINT_MESSAGE = "CHECK_POINT";
    private static final String OK = "200";
    private static final String OK_MESSAGE = "Request is valid.";
    private static final String CREATED = "201";
    private static final String CREATED_MESSAGE = "%s has been created.";
    private static final String ACCEPTED = "202";
    private static final String REQUEST_ACCEPTED = "Request has been accepted.";
    private static final String FOUND = "302";
    private static final String FOUND_MESSAGE = "%s was found!";
    private static final String SEE_OTHER = "303";
    private static final String SEE_OTHER_MESSAGE = "Check for more information at FAQ section.";
    private static final String NOT_MODIFIED = "304";
    private static final String NOT_MODIFIED_MESSAGE = "%s was not modified.";
    private static final String SWITCH_PROXY = "306";
    private static final String TEMP_REDIRECT = "307";
    private static final String TEMP_REDIRECT_MESSAGE = "Request has been temporarily redirected.";
    private static final String BAD_REQUEST = "400";
    private static final String BAD_REQUEST_MESSAGE = "Request was badly framed. Enter details in correct format.";
    private static final String UNAUTHORIZED = "401";
    private static final String UNAUTHORIZED_MESSAGE = "This request is unauthorized.";
    private static final String PAYMENT_REQUIRED = "402";
    private static final String PAYMENT_REQUIRED_MESSAGE = "This operation requires payment. Proceed to pay before continuing.";
    private static final String FORBIDDEN = "403";
    private static final String FORBIDDEN_MESSAGE = "This request is forbidden.";
    private static final String NOT_FOUND  = "404";
    private static final String NOT_FOUND_MESSAGE = "%s requested is not found.";
    private static final String NOT_ACCEPTABLE = "406";
    private static final String NOT_ACCEPTABLE_MESSAGE = "This operation is not acceptable.";
    private static final String GONE = "410";
    private static final String GONE_MESSAGE = "GONE";
    private static final String SERVER_ERROR = "500";
    private static final String SERVER_ERROR_MESSAGE = "SERVER ERROR";
    private static final String BAD_GATEWAY = "502";
    private static final String BAD_GATEWAY_MESSAGE = "BAD GATEWAY";
    private static final String TIME_OUT = "504";
    private static final String TIME_OUT_REQUEST = "REQUEST TIME OUT";



    //I don't know what this method is for yet....
    public static String generateBookSerialNumber(int len, String bookTitle) {

        String bookRegNumber = ""; //This will contain the book's registration number
        char[] numChar = new char[len]; //Array created to hold a maximum number accepted as len
        Random randomNumber = new Random();
        int x = 0; //Number to accept new random number generated

        for(int i = 0; i < len; i++){
            x = randomNumber.nextInt(9);
            numChar[i] = Integer.toString(x).toCharArray()[0]; //number generated is converted to character type
        }

        //The registration number will contain the first 3 characters of book's title which includes hyphen and numbers generated
        bookRegNumber = bookTitle.substring(0, 3).toUpperCase() + "-" + new String(numChar);

        return bookRegNumber.trim();

    }
}
