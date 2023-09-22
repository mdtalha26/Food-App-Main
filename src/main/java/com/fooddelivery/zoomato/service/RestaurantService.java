package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.Menu;
import com.fooddelivery.zoomato.entity.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
//    public List<Restaurant> getAllRestaurants();
    public List<Restaurant> getAllRestaurants(int pageNumber, String searchKey);
    public List<Restaurant> getMyRestaurants();
    public Optional<Restaurant> getRestaurantById(Integer restaurantId);
    public Restaurant createRestaurant(Restaurant restaurant);
    public Restaurant updateRestaurant(Restaurant restaurant);
    public void deleteRestaurant(Integer restaurantId);
    public List<Menu> getAllMenus();
    public Optional<Menu> getMenuById(Integer menuId);
    public Menu createMenu(Menu menu);
    public Menu updateMenu(Menu menu);
    public void deleteMenu(Integer menuId);

}

