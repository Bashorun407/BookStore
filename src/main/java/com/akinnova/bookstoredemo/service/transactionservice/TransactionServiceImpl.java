package com.akinnova.bookstoredemo.service.transactionservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.cartdto.CartItemPurchaseDto;
import com.akinnova.bookstoredemo.email.emailDto.EmailDetail;
import com.akinnova.bookstoredemo.email.emailService.EmailService;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.entity.Cart;
import com.akinnova.bookstoredemo.entity.QTransaction;
import com.akinnova.bookstoredemo.entity.Transaction;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CartRepository;
import com.akinnova.bookstoredemo.repository.TransactionRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;

import com.akinnova.bookstoredemo.service.transactionservice.ITransactionService;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private EntityManager entityManager;
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

    //2) Method to find transaction details by username
    @Override
    public ResponseEntity<?> transactionByUsername(String username) {

        List<Transaction> transactionList = transactionRepository.findByUsername(username).get();

        if(transactionList.isEmpty())
            return new ResponseEntity<>("There are no transactions yet", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    //3) Method to find transaction details by invoice code
    @Override
    public ResponseEntity<?> transactionByInvoiceCode(String transactionCode) {
        Transaction transaction = transactionRepository.findByInvoiceCode(transactionCode).get();

        if(ObjectUtils.isEmpty(transaction))
            return new ResponseEntity<>(String.format("There is no transaction for this invoice: %s", transactionCode),
                    HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(transaction, HttpStatus.NOT_FOUND);
    }

    //4) Method to search for transaction details with different parameters
    @Override
    public ResponseEntity<?> searchTransaction(String username, String invoiceCode, Pageable pageable) {

        QTransaction qTransaction = QTransaction.transaction;
        BooleanBuilder predicate = new BooleanBuilder();

        if(StringUtils.hasText(username))
            predicate.and(qTransaction.username.likeIgnoreCase("%" + username + "%"));

        if(StringUtils.hasText(invoiceCode))
            predicate.and(qTransaction.invoiceCode.eq(invoiceCode));

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<Transaction> jpaQuery = jpaQueryFactory.selectFrom(qTransaction)
                .where(predicate)
                .orderBy(qTransaction.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Page<Transaction> transactionPage = new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());

        if(transactionPage.isEmpty())
            return new ResponseEntity<>("Your search does not match any item", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(transactionPage, HttpStatus.FOUND);
    }
}
