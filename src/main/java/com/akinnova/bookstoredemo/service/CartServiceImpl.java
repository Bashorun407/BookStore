package com.akinnova.bookstoredemo.service;

import com.akinnova.bookstoredemo.Exception.ApiException;

import com.akinnova.bookstoredemo.dto.CartDto;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.entity.Cart;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CartRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl {

    private final CartRepository cartRepository;
    private final BookEntityRepository bookEntityRepository;

    //Class Constructor
    public CartServiceImpl(CartRepository cartRepository, BookEntityRepository bookEntityRepository) {
        this.cartRepository = cartRepository;
        this.bookEntityRepository = bookEntityRepository;
    }

    //1) Method to add item to cart
    public ResponsePojo<Cart> createCartItem(CartDto cartDto) {
        //Cart List to collect all books

        Optional<BookEntity> bookOptional =  bookEntityRepository.findBookByTitle(cartDto.getTitle());
        bookOptional.orElseThrow(()->
                new ApiException(String.format("Book with the title: %s does not exist.", cartDto.getTitle())));

        BookEntity bookEntity = bookOptional.get();
        //if it exists, extract needed details into cartDto object
        Cart cart = Cart.builder()
                .username(cartDto.getUsername())
                .title(cartDto.getTitle())
                .serialNumber(bookEntity.getSerialNumber())
                .cartItemNumber(ResponseUtils.generateInvoiceCode(5, cartDto.getUsername()))
                .quantity(cartDto.getQuantity())
                .price(bookEntity.getPrice())
                .amountToPay(cartDto.getPrice() * cartDto.getQuantity())
                .checkOut(false)
                .timeCheckedIn(LocalDateTime.now())
                .build();

        //Save data to repository
        Cart addedCart = cartRepository.save(cart);

        ResponsePojo<Cart> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.CREATED);
        responsePojo.setMessage("Added to cart");
        responsePojo.setData(addedCart);

        return responsePojo;
    }


    //2) Method to retrieve all items in cart by username (i.e. for a username)
    public ResponsePojo<List<Cart>> getCartItemByUsername(String username) {
        //To retrieve all items in the cart for a username
        Optional<List<Cart>> cartListOptional = cartRepository.findByUsername(username);
        cartListOptional.orElseThrow(()-> new ApiException(String.format("No item was saved by %s in the database.", username)));

        List<Cart> cartList = cartListOptional.get();
        ResponsePojo<List<Cart>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("Items in Cart: ");
        responsePojo.setData(cartList);
        return responsePojo;
    }


//3) Method to update Item in cart
// TODO: 7/5/2023 Cart needs some improvement 
    public ResponsePojo<Cart> updateCartItem(CartDto cartDto) {
        //To retrieve the item to update from the database
        Optional<Cart> itemOptional = cartRepository.findByTitle(cartDto.getTitle());
        itemOptional.orElseThrow(()-> new ApiException(String.format("Book with this title does not exist in the cart.")));

        Cart itemToUpdate = itemOptional.get();

        //Updating the contents of the itemToUpdate
        itemToUpdate.setTitle(cartDto.getTitle());
        itemToUpdate.setSerialNumber(cartDto.getSerialNumber());
        itemToUpdate.setPrice(cartDto.getPrice());
        itemToUpdate.setQuantity(cartDto.getQuantity());
        itemToUpdate.setAmountToPay(cartDto.getAmountToPay());

        //Saving updated contents to cart database
        Cart savedCart = cartRepository.save(itemToUpdate);

        ResponsePojo<Cart> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.ACCEPTED);
        responsePojo.setMessage(ResponseUtils.REQUEST_ACCEPTED);
        responsePojo.setData(savedCart);
        return responsePojo;
    }

    //4) Method to delete/remove item from cart
    public ResponseEntity<?> removeFromCart(String title) {

        //To retrieve the item to delete from the database
        Optional<Cart> itemOptional = cartRepository.findByTitle(title);
        itemOptional.orElseThrow(()-> new ApiException(String.format("book with title: %s not found in cart.", title)));

        //Remove item from cart in database
        cartRepository.delete(itemOptional.get());
        return new ResponseEntity<>("Item removed from Cart", HttpStatus.GONE);
    }

}
