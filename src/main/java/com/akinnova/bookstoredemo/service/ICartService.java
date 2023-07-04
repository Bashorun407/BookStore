//package com.akinnova.bookstoredemo.service;
//
//import com.akinnova.bookstoredemo.dto.CartDto;
//import com.akinnova.bookstoredemo.entity.Cart;
//import com.akinnova.bookstoredemo.response.ResponsePojo;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//public interface ICartService {
//    ResponsePojo<Cart> createCartItem(CartDto cartDto);
//    ResponsePojo<List<Cart>> getCartItemByUsername(String username);
//    ResponsePojo<Cart> updateCartItem(String serialNumber, CartDto cartDto);
//    ResponseEntity<?> removeFromCart(String serialNumber);
//
//}
