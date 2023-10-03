package com.fooddelivery.zoomato.controller;

import com.fooddelivery.zoomato.costomexceptions.FssaiNumberAlreadyExistsException;
import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.ImageModel;
import com.fooddelivery.zoomato.entity.Menu;
import com.fooddelivery.zoomato.entity.Restaurant;
import com.fooddelivery.zoomato.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fooddelivery.zoomato.service.MenuService;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @PreAuthorize("hasAnyRole('Admin', 'User')")
    @GetMapping
//    public List<Restaurant> getAllRestaurant() {
//        return restaurantService.getAllRestaurants();
//    }
    public List<Restaurant> getAllRestaurant(@RequestParam(defaultValue = "0") int pageNumber,
                                          @RequestParam(defaultValue = "") String searchKey) {
        List<Restaurant> result = restaurantService.getAllRestaurants(pageNumber, searchKey);
        System.out.println("Result size is "+ result.size());
        return result;
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @GetMapping("/{restaurantId}")
    public Optional<Restaurant> getRestaurantById(@PathVariable Integer restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

//    @PreAuthorize("hasRole('Admin')")
//    @PostMapping
//    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
//        return restaurantService.createRestaurant(restaurant);
//    }

    @PreAuthorize("hasRole('Restaurant')")
    @GetMapping("/myRestaurants")
    public List<Restaurant> getMyRestaurants(){
        return restaurantService.getMyRestaurants();
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createRestaurant(@RequestPart("restaurant") Restaurant restaurant,
                                                      @RequestPart("imageFile") MultipartFile[] file) {
        try {
            Set<ImageModel> images = uploadImage(file);
            restaurant.setDocuments(images);
            return ResponseEntity.ok(restaurantService.createRestaurant(restaurant));
        } catch (FssaiNumberAlreadyExistsException f){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(f.getMessage());
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PutMapping("/{restaurantId}")
    public Restaurant updateRestaurant(@PathVariable Integer restaurantId, @RequestBody Restaurant restaurant) {
        restaurant.setRestaurantId(restaurantId);
        return restaurantService.updateRestaurant(restaurant);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @DeleteMapping("/{restaurantId}")
    public void deleteRestaurant(@PathVariable Integer restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
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

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PostMapping("/myRestaurants/{restaurantId}/menus")
    public Restaurant createMenuInRestaurant(@PathVariable Integer restaurantId, @RequestBody Menu menu) {
        Optional<Restaurant> restOptional = restaurantService.getRestaurantById(restaurantId);
        restOptional.ifPresent(restaurant -> {
            restaurant.getMenus().add(menu);
            restaurantService.updateMenu(menu);
        });
        return restOptional.orElse(null);
//        return restaurantService.createMenu(menu);
    }



    @PreAuthorize("hasAnyRole('Admin', 'Restaurant','User')")
    @GetMapping("/myRestaurants/{restaurantId}/menus")
    public List<Menu> getRestaurantMenus(@PathVariable Integer restaurantId) {
        Optional<Restaurant> restOptional = restaurantService.getRestaurantById(restaurantId);
        return restOptional.map(Restaurant::getMenus).orElse(null);
    }


//    @PreAuthorize("hasRole('Admin')")
//    @PutMapping("/{menuId}/fooditems/{foodItemId}")
//    public Menu updateMenuInRestaurant(
//            @PathVariable Integer menuId,
//            @PathVariable Integer foodItemId,
//            @RequestBody FoodItem foodItem) {
//        Optional<Menu> menuOptional = menuService.getMenuById(menuId);
//        menuOptional.ifPresent(menu -> {
//            Optional<FoodItem> foodItemOptional = menu.getFoodItems().stream()
//                    .filter(item -> item.getFoodItemId().equals(foodItemId))
//                    .findFirst();
//            foodItemOptional.ifPresent(item -> {
//                item.setFoodItemName(foodItem.getFoodItemName());////if error change set/getFoodItemName to set/getName
//                item.setFoodItemPrice(foodItem.getFoodItemPrice());////here too
//                menuService.updateMenu(menu);
//            });
//        });
//        return menuOptional.orElse(null);
//    }
}
