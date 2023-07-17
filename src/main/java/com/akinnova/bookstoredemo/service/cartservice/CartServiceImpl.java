package com.akinnova.bookstoredemo.service.cartservice;

import com.akinnova.bookstoredemo.Exception.ApiException;

import com.akinnova.bookstoredemo.dto.cartdto.CartDto;
import com.akinnova.bookstoredemo.entity.BookEntity;
import com.akinnova.bookstoredemo.entity.Cart;
import com.akinnova.bookstoredemo.entity.QCart;
import com.akinnova.bookstoredemo.repository.BookEntityRepository;
import com.akinnova.bookstoredemo.repository.CartRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;

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
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private EntityManager entityManager;
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
                .cartItemNumber(ResponseUtils.generateUniqueIdentifier(5, cartDto.getUsername()))
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

    //5) Method to search cart using multiple parameters
    @Override
    public ResponsePojo<Page<Cart>> searchCart(String username, String title, String serialNumber,
                                               String cartItemNumber, Pageable pageable) {
        QCart qCart = QCart.cart;
        BooleanBuilder predicate = new BooleanBuilder();

        if(StringUtils.hasText(username))
            predicate.and(qCart.username.likeIgnoreCase("%" + username + "%"));

        if(StringUtils.hasText(title))
            predicate.and(qCart.title.likeIgnoreCase("%" + title + "%"));

        if(StringUtils.hasText(serialNumber))
            predicate.and(qCart.serialNumber.eq(serialNumber));

        if(StringUtils.hasText(cartItemNumber))
            predicate.and(qCart.cartItemNumber.eq(cartItemNumber));

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<Cart> jpaQuery = jpaQueryFactory.selectFrom(qCart)
                .where(predicate.and(qCart.checkOut.eq(false)))
                .orderBy(qCart.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Page<Cart> cartPage = new PageImpl<>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());

        //ResponsePoJo
        ResponsePojo<Page<Cart>> responsePojo = new ResponsePojo<>();
        responsePojo.setStatusCode(ResponseUtils.FOUND);
        responsePojo.setMessage("Cart items: ");
        responsePojo.setData(cartPage);

        return responsePojo;
    }

}
