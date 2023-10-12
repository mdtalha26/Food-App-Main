package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.entity.Cart;
import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.Restaurant;
import com.fooddelivery.zoomato.entity.User;
import com.fooddelivery.zoomato.repository.CartRepository;
import com.fooddelivery.zoomato.repository.FoodItemRepository;
import com.fooddelivery.zoomato.repository.RestaurantRepository;
import com.fooddelivery.zoomato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodItemServiceImpl implements FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CartRepository cartRepository;

    public FoodItem addNewFoodItem(FoodItem foodItem) {
        String username = JwtRequestFilter.CURRENT_USER;

        User user = null;
        if(username != null) {
            user = userRepository.findById(username).get();
        }
        foodItem.setUser(user);
        return foodItemRepository.save(foodItem);
    }

    public List<FoodItem> getAllFoodItems(int pageNumber, String searchKey, String category) {
        Pageable pageable = PageRequest.of(pageNumber,12);
        if(category.equals("")) {
            if (searchKey.equals("")) {
                return (List<FoodItem>) foodItemRepository.findAll(pageable);
            } else {
                return (List<FoodItem>) foodItemRepository.findByFoodItemNameContainingIgnoreCaseOrFoodItemDescriptionContainingIgnoreCase(
                        searchKey, searchKey, pageable
                );
            }
        }else {
            return (List<FoodItem>) foodItemRepository.findAllByFoodItemCategory(category,pageable);
        }

    }

    //////////////////////////////////////////////////////////////////////////////

    public List<Restaurant> getAllRestaurantOfFoodItems(int pageNumber, String searchKey, String category) {
        Pageable pageable = PageRequest.of(pageNumber,12);
        if(category.equals("")) {
            if (searchKey.equals("")) {
                return restaurantRepository.findAll(pageable);
            } else {
                List<FoodItem> fiList= foodItemRepository.findByFoodItemNameContainingIgnoreCaseOrFoodItemDescriptionContainingIgnoreCase(
                        searchKey, searchKey, pageable
                );
                List<User> filteredList= fiList.stream().map(x->x.getUser()).collect(Collectors.toList());
                List<Restaurant> restList=restaurantRepository.findAll(pageable);
                List<Restaurant> restaurantsWithMatchingUsers = restList.stream()
                        .filter(restaurant -> filteredList.contains(restaurant.getUser()))
                        .collect(Collectors.toList());
                return restaurantsWithMatchingUsers;
            }
        }else {
            List<FoodItem> fiByCategory= foodItemRepository.findAllByFoodItemCategory(category,pageable);
            List<User> filteredList= fiByCategory.stream().map(x->x.getUser()).collect(Collectors.toList());
            List<Restaurant> restList=restaurantRepository.findAll(pageable);
            List<Restaurant> restaurantsWithMatchingUsers = restList.stream()
                    .filter(restaurant -> filteredList.contains(restaurant.getUser()))
                    .collect(Collectors.toList());
            return restaurantsWithMatchingUsers;
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    public FoodItem getFoodItemDetailsById(Integer foodItemId) {
        return foodItemRepository.findById(foodItemId).get();
    }

    public void deleteFoodItemDetails(Integer foodItemId) {
        foodItemRepository.deleteById(foodItemId);
    }

    public List<FoodItem> getFoodItemDetails(boolean isSingleProductCheckout, Integer foodItemId) {
        if(isSingleProductCheckout && foodItemId != 0) {
            // we are going to buy a single product

            List<FoodItem> list = new ArrayList<>();
            FoodItem foodItem = foodItemRepository.findById(foodItemId).get();
            list.add(foodItem);
            return list;
        } else {
            // we are going to checkout entire cart
            String username = JwtRequestFilter.CURRENT_USER;
            User user = userRepository.findById(username).get();
            List<Cart> carts = cartRepository.findByUser(user);

            return carts.stream().map(x -> x.getFoodItem()).collect(Collectors.toList());
        }
    }

    public List<FoodItem> getFoodItemsByCategory(String category, int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber,12);
        return foodItemRepository.findAllByFoodItemCategory(category,pageable);
    }
}
