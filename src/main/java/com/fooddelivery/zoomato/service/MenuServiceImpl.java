package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.Menu;
import com.fooddelivery.zoomato.repository.FoodItemRepository;
import com.fooddelivery.zoomato.repository.MenuRepository;
import com.fooddelivery.zoomato.utility.GlobalResources;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final FoodItemRepository foodItemRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository, FoodItemRepository foodItemRepository) {
        this.menuRepository = menuRepository;
        this.foodItemRepository = foodItemRepository;
    }
    private Logger logger=GlobalResources.getLogger(FoodItemServiceImpl.class);

    public List<Menu> getAllMenus() {
        String methodName="getAllMenus()";
        logger.info(methodName+"Called");
        return menuRepository.findAll();
    }

    public Optional<Menu> getMenuById(Integer menuId) {
        String methodName="getMenuById()";
        logger.info(methodName+"Called");
//        Optional<Menu> op = menuRepository.findById(menuId);
//        if (op.isPresent())
//            return op.get();
//        else
//            throw new FoodItemIdNotFoundException("Menu not found for this Id:" + menuId);
        return menuRepository.findById(menuId);
    }

    //    public String createMenu(Menu menu){
    public Menu createMenu(Menu menu) {
        String methodName="createMenu()";
        logger.info(methodName+"Called");
        return menuRepository.save(menu);
//        menuRepository.save(menu);
//        return "Menu created Successfully";
    }

    public Menu updateMenu(Menu menu) {
        String methodName="updateMenu()";
        logger.info(methodName+"Called");
        return menuRepository.save(menu);
//        return "Menu updated successfully";
    }

    public void deleteMenu(Integer menuId) {
        String methodName="deleteMenu()";
        logger.info(methodName+"Called");
        menuRepository.deleteById(menuId);
//        return "Menu deleted successfully";
    }

//    public List<FoodItem> getAllFoodItems() {
//        String methodName="getAllFoodItems()";
//        logger.info(methodName+"Called");
//        return foodItemRepository.findAll();
//    }

    public Optional<FoodItem> getFoodItemById(Integer foodItemId) {
        String methodName="getFoodItemById()";
        logger.info(methodName+"Called");
        return foodItemRepository.findById(foodItemId);
    }

    public FoodItem createFoodItem(FoodItem foodItem) {
        String methodName="createFoodItem()";
        logger.info(methodName+"Called");
        return foodItemRepository.save(foodItem);
//        return "Food item created successfully";
    }

    public FoodItem updateFoodItem(FoodItem foodItem) {
        String methodName="updateFoodItem()";
        logger.info(methodName+"Called");
        return foodItemRepository.save(foodItem);
//        return "Food item updated successfully";
    }

    public void deleteFoodItem(Integer foodItemId) {
        String methodName="deleteFoodItem()";
        logger.info(methodName+"Called");
        foodItemRepository.deleteById(foodItemId);
//        return "Food item deleted successfully";
    }

    //////////////////////////////////////////////trial code 1/////////////////////////
    public List<FoodItem> getAllFoodItemInMenu(Integer menuId,String searchKey,int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber,10);
        if(searchKey.equals("")){
            return menuRepository.findFoodItemByMenuId(menuId,pageable);
        }else {
            return menuRepository.findFilteredFoodItemsByMenuIdAndSearchKey(menuId,searchKey.toLowerCase(),pageable);
        }

    }

    /////////////////////////////////////////////// code 2 for backup/////////////////////////////

    public List<FoodItem> getAllFoodItemInMenuBySearch(Integer menuId, String searchKey, int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber,10);
        return menuRepository.findFilteredFoodItemsByMenuIdAndSearchKey(menuId,searchKey.toLowerCase(),pageable);
    }
}
