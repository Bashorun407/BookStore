package com.akinnova.bookstoredemo.service.cartservice;

import com.akinnova.bookstoredemo.dto.cartdto.CartDto;
import com.akinnova.bookstoredemo.dto.cartdto.CartResponseDto;
import com.akinnova.bookstoredemo.entity.Cart;
import com.akinnova.bookstoredemo.response.ResponsePojo;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface ICartService {
    ResponsePojo<CartResponseDto> createCartItem(CartDto cartDto);
    ResponseEntity<?> getCartItemByUsername(String username);
    ResponsePojo<CartResponseDto> updateCartItem(CartDto cartDto);
    ResponseEntity<?> removeFromCart(String title);
    ResponseEntity<?> searchCart(String username, String title, String serialNumber, String cartItemNumber, Pageable pageable);
}
