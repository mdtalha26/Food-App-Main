package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.Menu;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    public List<Menu> getAllMenus();
    public Optional<Menu> getMenuById(Integer menuId);
    public Menu createMenu(Menu menu);
    public Menu updateMenu(Menu menu);
    public void deleteMenu(Integer menuId);

    public List<FoodItem> getAllFoodItemInMenu(Integer menuId,String searchKey, int pageNumber);

    public List<FoodItem> getAllFoodItemInMenuBySearch(Integer menuId, String searchKey, int pageNumber);
    public Optional<FoodItem> getFoodItemById(Integer foodItemId);
    public FoodItem createFoodItem(FoodItem foodItem);
    public FoodItem updateFoodItem(FoodItem foodItem);
    public void deleteFoodItem(Integer foodItemId);
}

