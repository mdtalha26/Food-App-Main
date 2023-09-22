package com.fooddelivery.zoomato.controller;

import com.fooddelivery.zoomato.service.FoodItemService;
import com.fooddelivery.zoomato.entity.ImageModel;
import com.fooddelivery.zoomato.entity.FoodItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PostMapping(value = {"/addNewFoodItem"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public FoodItem addNewFoodItem(@RequestPart("foodItem") FoodItem foodItem,
                                 @RequestPart("imageFile") MultipartFile[] file) {
        try {
            Set<ImageModel> images = uploadImage(file);
            foodItem.setFoodItemImages(images);
            return foodItemService.addNewFoodItem(foodItem);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile file: multipartFiles) {
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }

        return imageModels;
    }

    @GetMapping({"/getAllFoodItems"})
    public List<FoodItem> getAllFoodItems(@RequestParam(defaultValue = "0") int pageNumber,
                                        @RequestParam(defaultValue = "") String searchKey) {
        List<FoodItem> result = foodItemService.getAllFoodItems(pageNumber, searchKey);
        System.out.println("Result size is "+ result.size());
        return result;
    }

    @GetMapping({"/getFoodItemDetailsById/{foodItemId}"})
    public FoodItem getFoodItemDetailsById(@PathVariable("foodItemId") Integer foodItemId) {
        return foodItemService.getFoodItemDetailsById(foodItemId);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @DeleteMapping({"/deleteFoodItemDetails/{foodItemId}"})
    public void deleteFoodItemDetails(@PathVariable("foodItemId") Integer foodItemId) {
        foodItemService.deleteFoodItemDetails(foodItemId);
    }

    @PreAuthorize("hasAnyRole('User')")
    @GetMapping({"/getFoodItemDetails/{isSingleProductCheckout}/{foodItemId}"})
    public List<FoodItem> getFoodItemDetails(@PathVariable(name = "isSingleProductCheckout" ) boolean isSingleProductCheckout,
                                           @PathVariable(name = "foodItemId")  Integer foodItemId) {
        return foodItemService.getFoodItemDetails(isSingleProductCheckout, foodItemId);
    }
}
