package com.akinnova.bookstoredemo.response;

import java.util.Random;

public class ResponseUtils {
    //The following are HTTP status codes
    public static final String CONTINUE = "100";
    public static final String CONTINUE_MESSAGE = "CONTINUE";
    public static final String SWITCHING = "101";
    public static final String SWITCHING_MESSAGE = "SWITCHING";
    public static final String CHECK_POINT = "103";
    public static final String CHECK_POINT_MESSAGE = "CHECK_POINT";
    public static final String OK = "200";
    public static final String OK_MESSAGE = "Request is valid.";
    public static final String CREATED = "201";
    public static final String CREATED_MESSAGE = "%s has been created.";
    public static final String ACCEPTED = "202";
    public static final String REQUEST_ACCEPTED = "Request has been accepted.";
    public static final String FOUND = "302";
    public static final String FOUND_MESSAGE = "%s was found!";
    public static final String SEE_OTHER = "303";
    public static final String SEE_OTHER_MESSAGE = "Check for more information at FAQ section.";
    public static final String NOT_MODIFIED = "304";
    public static final String NOT_MODIFIED_MESSAGE = "%s was not modified.";
    public static final String SWITCH_PROXY = "306";
    public static final String TEMP_REDIRECT = "307";
    public static final String TEMP_REDIRECT_MESSAGE = "Request has been temporarily redirected.";
    public static final String BAD_REQUEST = "400";
    public static final String BAD_REQUEST_MESSAGE = "Request was badly framed. Enter details in correct format.";
    public static final String UNAUTHORIZED = "401";
    public static final String UNAUTHORIZED_MESSAGE = "This request is unauthorized.";
    public static final String PAYMENT_REQUIRED = "402";
    public static final String PAYMENT_REQUIRED_MESSAGE = "This operation requires payment. Proceed to pay before continuing.";
    public static final String FORBIDDEN = "403";
    public static final String FORBIDDEN_MESSAGE = "This request is forbidden.";
    public static final String NOT_FOUND  = "404";
    public static final String NOT_FOUND_MESSAGE = "%s requested is not found.";
    public static final String NOT_ACCEPTABLE = "406";
    public static final String NOT_ACCEPTABLE_MESSAGE = "This operation is not acceptable.";
    public static final String GONE = "410";
    public static final String GONE_MESSAGE = "GONE";
    public static final String SERVER_ERROR = "500";
    public static final String SERVER_ERROR_MESSAGE = "SERVER ERROR";
    public static final String BAD_GATEWAY = "502";
    public static final String BAD_GATEWAY_MESSAGE = "BAD GATEWAY";
    public static final String TIME_OUT = "504";
    public static final String TIME_OUT_REQUEST = "REQUEST TIME OUT";



    //Method to generate a book's Serial Number
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

    //Method to generate invoice code in Transaction Service layer
    public static String generateInvoiceCode(int len, String username){
        String invoiceCode = ""; //newly generated invoice-code will be stored in this variable
        char[] numChar = new char[len]; //Character array that will hold a maximum of 'len' characters
        Random random = new Random();
        int x = 0; //x will contain new random number generated

        for (int i = 0; i < len; i++){
            x = random.nextInt(1, 6); //random numbers generated will be from (1 - 6)
            numChar[i] = Integer.toString(x).toCharArray()[0];
        }
        //Invoice will be a combination of the first 3-characters of username with randomly generated digits
        invoiceCode = username.substring(0, 2) + new String(numChar);
        return invoiceCode;
    }

    //Method to generate unique identifier in Transaction Service layer
    public static String generateUniqueIdentifier(int len, String username){
        String invoiceCode = ""; //newly generated invoice-code will be stored in this variable
        char[] numChar = new char[len]; //Character array that will hold a maximum of 'len' characters
        Random random = new Random();
        int x = 0; //x will contain new random number generated

        for (int i = 0; i < len; i++){
            x = random.nextInt(1, 6); //random numbers generated will be from (1 - 6)
            numChar[i] = Integer.toString(x).toCharArray()[0];
        }
        //Invoice will be a combination of the first 3-characters of username with randomly generated digits
        invoiceCode = username.substring(0, 2) + new String(numChar);
        return invoiceCode;
    }

    //Method to generate Like in Review Service class
    public static int likeFunction(int like) {
        return like == 1 ? 1 : 0;
    }

    //Method to generate rate in Review Service class
    public static int rateFunction(int rate){
        if(rate >= 1  && rate <=5){
            return rate;
        }
        //else return 0
        else
            return 0;
    }

    //Compact form of rateFunction
    public int ratoFunc(int rate){
        return (rate >= 1 && rate <= 5) ? rate : 0;
    }

}
