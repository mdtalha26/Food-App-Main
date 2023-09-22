package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.repository.CartRepository;
import com.fooddelivery.zoomato.repository.FoodItemRepository;
import com.fooddelivery.zoomato.repository.UserRepository;
import com.fooddelivery.zoomato.entity.Cart;
import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public interface CartService {

    public void deleteCartItem(Integer cartId);

    public Cart addToCart(Integer foodItemId);

    public List<Cart> getCartDetails();
}
