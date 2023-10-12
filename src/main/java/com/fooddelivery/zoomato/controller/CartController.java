package com.fooddelivery.zoomato.controller;

import com.fooddelivery.zoomato.costomexceptions.DifferentRestaurantCantBeAddedToCartException;
import com.fooddelivery.zoomato.costomexceptions.FoodItemAlreadyInCartException;
import com.fooddelivery.zoomato.entity.Cart;
import com.fooddelivery.zoomato.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/addToCart/{foodItemId}"})
    public ResponseEntity<?> addToCart(@PathVariable(name = "foodItemId") Integer foodItemId) {
        try {
            return ResponseEntity.ok(cartService.addToCart(foodItemId));
        }catch (DifferentRestaurantCantBeAddedToCartException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (FoodItemAlreadyInCartException f){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(f.getMessage());
        }
    }

    @PreAuthorize("hasRole('User')")
    @DeleteMapping({"/deleteCartItem/{cartId}"})
    public void deleteCartItem(@PathVariable(name = "cartId") Integer cartId) {
        cartService.deleteCartItem(cartId);
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getCartDetails"})
    public List<Cart> getCartDetails() {
        return cartService.getCartDetails();
    }
}
