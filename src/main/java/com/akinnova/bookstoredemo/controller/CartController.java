package com.akinnova.bookstoredemo.controller;

import com.akinnova.bookstoredemo.dto.CartDto;
import com.akinnova.bookstoredemo.entity.Cart;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart/auth")
public class CartController {

    @Autowired
    private CartServiceImpl cartService;

    //1) Method to add item to cart
    @PostMapping("/addCart")
    public ResponsePojo<Cart> createCartItem(@RequestBody CartDto cartDto) {
        return cartService.createCartItem(cartDto);
    }

    //2) Method to retrieve all items in cart by username (i.e. for a username)
    @GetMapping("/cartItems/{username}")
    public ResponsePojo<List<Cart>> getCartItemByUsername(@PathVariable(name = "username") String username) {
        return cartService.getCartItemByUsername(username);
    }

    //3) Method to update Item in cart
    @PutMapping("/updateCart")
    public ResponsePojo<Cart> updateCartItem(@RequestBody CartDto cartDto) {
        return cartService.updateCartItem(cartDto);
    }

    //4) Method to delete/remove item from cart
    @DeleteMapping("/delCart/{serialNumber}")
    public ResponseEntity<?> removeFromCart(@PathVariable(name = "serialNumber") String serialNumber) {
        return cartService.removeFromCart(serialNumber);
    }
}


