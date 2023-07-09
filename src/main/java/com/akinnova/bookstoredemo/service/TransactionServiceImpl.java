package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.CartItemPurchaseDto;
import com.akinnova.bookstoredemo.email.emailDto.EmailDetail;
import com.akinnova.bookstoredemo.email.emailService.EmailService;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.entity.Cart;
import com.akinnova.bookstoredemo.entity.Transaction;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CartRepository;
import com.akinnova.bookstoredemo.repository.TransactionRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TransactionServiceImpl implements ITransactionService{
    private final TransactionRepository transactionRepository;
    private final CartRepository cartRepository;
    private final BookEntityRepository bookEntityRepository;
    private final EmailService emailService;

    //Class Constructor
    public TransactionServiceImpl(TransactionRepository transactionRepository, CartRepository cartRepository,
                                  BookEntityRepository bookEntityRepository, EmailService emailService) {
        this.transactionRepository = transactionRepository;
        this.cartRepository = cartRepository;
        this.bookEntityRepository = bookEntityRepository;
        this.emailService = emailService;
    }

    //1) Method to conduct transaction
    @Override
    public ResponsePojo<Transaction> cashPayment(CartItemPurchaseDto cartItemPurchaseDto) {

        // TODO: 7/2/2023 (Retrieve cart items, change checkout status to true and make payment via customer card
        //to retrieve cart items using username
        List<Cart> cartList = cartRepository.findByUsername(cartItemPurchaseDto.getUsername()).get();

        //To find the sum of items to buy
        Double amountToPay = cartList.stream().mapToDouble(Cart::getPrice).sum();

        //To check that customer balance is equal to or more than the amountToPay
        //If customer's balance is less than amount to pay...throw an exception
        if (cartItemPurchaseDto.getBalance() < amountToPay){
            throw new ApiException("Your balance is not sufficient to make purchase.");
        }

        //To change the checkout attribute of cart items in cart repository and then save in the database (cart database)
        for (Cart item: cartList) {
            Cart cartItem = cartRepository.findByCartItemNumber(item.getCartItemNumber()).get();
            cartItem.setCheckOut(true);
            //Remove sold items cart items database
            cartRepository.delete(cartItem);
            //Save new update in cart repository
            //cartRepository.save(cartItem);
        }

        //Remove the quantity of items bought from total quantity in bookEntity repository and then save update in the database
        for(Cart item: cartList){
            BookEntity bookEntity = bookEntityRepository.findBookBySerialNumber(item.getSerialNumber()).get();
            //reduce book quantity by quantity of books sold
                    bookEntity.setQuantity(bookEntity.getQuantity() - item.getQuantity());

                    //save the new information in the repository
            bookEntityRepository.save(bookEntity);
        }

        //Deduct the balance from customer's balance
        cartItemPurchaseDto.setBalance(cartItemPurchaseDto.getBalance() - amountToPay);

        String amountPaid = amountToPay.toString();
        //Send email to notify payment to customer
        EmailDetail emailDetail = EmailDetail.builder()
                .subject("Successful Purchase From Akinova BookStores")
                                .body("Dear " + cartItemPurchaseDto.getName() +
                                "\n You received this notification from a purchase you made on our online bookstore.\n"
                                + "Details of purchase are :"
                                        + "\n Cost of purchase: " + amountPaid
                                        + "\n Balance: " + cartItemPurchaseDto.getBalance().toString()
                                        + "\n Thank you for patronizing us."
                                + "\n\n Akinnova BookStore."  )
                .recipient(cartItemPurchaseDto.getEmail())
                .build();
        emailService.sendSimpleEmail(emailDetail);

        //Transaction details
        Transaction transaction = Transaction.builder()
                .username(cartItemPurchaseDto.getUsername())
                .amountPaid(amountToPay)
                .invoiceCode(ResponseUtils.generateInvoiceCode(6, cartItemPurchaseDto.getUsername()))
                .transactionDate(LocalDateTime.now())
                .build();

        //Save transaction in the Transaction repository
        transactionRepository.save(transaction);

        ResponsePojo<Transaction> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.OK);
        responsePojo.setMessage("Transaction is successful.");
        responsePojo.setData(transaction);
        return responsePojo;
    }
}
