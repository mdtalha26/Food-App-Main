package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.repository.CartRepository;
import com.fooddelivery.zoomato.repository.FoodItemRepository;
import com.fooddelivery.zoomato.repository.UserRepository;
import com.fooddelivery.zoomato.entity.Cart;
import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public interface FoodItemService {


    public FoodItem addNewFoodItem(FoodItem foodItem);

    public List<FoodItem> getAllFoodItems(int pageNumber, String searchKey);

    public FoodItem getFoodItemDetailsById(Integer foodItemId);

    public void deleteFoodItemDetails(Integer foodItemId);

    public List<FoodItem> getFoodItemDetails(boolean isSingleProductCheckout, Integer foodItemId);
}
