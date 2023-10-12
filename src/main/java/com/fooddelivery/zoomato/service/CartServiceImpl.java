package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.costomexceptions.DifferentRestaurantCantBeAddedToCartException;
import com.fooddelivery.zoomato.costomexceptions.FoodItemAlreadyInCartException;
import com.fooddelivery.zoomato.entity.Cart;
import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.User;
import com.fooddelivery.zoomato.repository.CartRepository;
import com.fooddelivery.zoomato.repository.FoodItemRepository;
import com.fooddelivery.zoomato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    public void deleteCartItem(Integer cartId) {
        cartRepository.deleteById(cartId);
    }

    public Cart addToCart(Integer foodItemId) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId).get();

        String username = JwtRequestFilter.CURRENT_USER;

        User user = null;
        if(username != null) {
            user = userRepository.findById(username).get();
        }

        List<Cart> cartList = cartRepository.findByUser(user);
        List<Cart> filteredList = cartList.stream().filter(x -> x.getFoodItem().getFoodItemId() == foodItemId).collect(Collectors.toList());

        if(filteredList.size() > 0) {
            throw new FoodItemAlreadyInCartException("FoodItem already present in cart, to adjust the quantity proceed to checkout");
//            return null;
        }

        if(foodItem != null && user != null) {
            Cart cart = new Cart(foodItem, user);
            if(cartList.isEmpty()) {
                return cartRepository.save(cart);
            }else{
                User restInCart = cart.getFoodItem().getUser();
                boolean restAlreadyInCart = cartList.stream().anyMatch(x -> x.getFoodItem().getUser().equals(restInCart));
                if(restAlreadyInCart){
                    return cartRepository.save(cart);
                }else {
                    throw new DifferentRestaurantCantBeAddedToCartException("Food from different restaurant cant be added. Either empty the cart or add food from same restaurant");
                }
            }
        }

        return null;
    }

    public List<Cart> getCartDetails() {
        String username = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(username).get();
        return cartRepository.findByUser(user);
    }
}
