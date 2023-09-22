package com.fooddelivery.zoomato.controller;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.ImageModel;
import com.fooddelivery.zoomato.entity.Menu;
import com.fooddelivery.zoomato.entity.User;
import com.fooddelivery.zoomato.repository.UserRepository;
import com.fooddelivery.zoomato.service.FoodItemService;
import com.fooddelivery.zoomato.service.MenuService;
import com.fooddelivery.zoomato.service.MenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private final MenuService menuService;
    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public MenuController(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @GetMapping("/{menuId}")
    public Optional<Menu> getMenuById(@PathVariable Integer menuId) {
        return menuService.getMenuById(menuId);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PostMapping
    public Menu createMenu(@RequestBody Menu menu) {
        return menuService.createMenu(menu);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PutMapping("/{menuId}")
    public Menu updateMenu(@PathVariable Integer menuId, @RequestBody Menu menu) {
        menu.setMenuId(menuId);
        return menuService.updateMenu(menu);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @DeleteMapping("/{menuId}")
    public void deleteMenu(@PathVariable Integer menuId) {
        menuService.deleteMenu(menuId);
    }


    /////////////////////////// Trial code 1 from here///////////////////////////////////////////////
    @GetMapping("/{menuId}/foodItemInMenu")
    public List<FoodItem> getAllFoodItemInMenu(@PathVariable Integer menuId,
                                               @RequestParam(defaultValue = "") String searchKey,
                                               @RequestParam(defaultValue = "0") int pageNumber
                                               ) {
        return menuService.getAllFoodItemInMenu(menuId,searchKey,pageNumber);
    }

    ///////////////////////////////////code 2 from here for backup//////////////////////////////////////////////////////

    @GetMapping("/{menuId}/foodItemInMenuBySearch")
    public List<FoodItem> getAllFoodItemInMenuBySearch(@PathVariable Integer menuId,
                                               @RequestParam String searchKey,
                                               @RequestParam(defaultValue = "0") int pageNumber
    ) {
        return menuService.getAllFoodItemInMenuBySearch(menuId,searchKey,pageNumber);
    }


    /////////////////////////// Trial code till here///////////////////////////////////////////////
    @GetMapping("/{menuId}/fooditems")
    public List<FoodItem> getAllFoodItems(@PathVariable Integer menuId) {
        Optional<Menu> menuOptional = menuService.getMenuById(menuId);
        return menuOptional.map(Menu::getFoodItems).orElse(null);
    }


    @GetMapping("/{menuId}/fooditems/{foodItemId}")
    public Optional<FoodItem> getFoodItemById(@PathVariable Integer menuId, @PathVariable Integer foodItemId) {
        Optional<Menu> menuOptional = menuService.getMenuById(menuId);
        return menuOptional.flatMap(menu -> menu.getFoodItems().stream()
                .filter(foodItem -> foodItem.getFoodItemId().equals(foodItemId))
                .findFirst());
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PostMapping("/{menuId}/fooditems")
    public Menu addFoodItemToMenu(@PathVariable Integer menuId, @RequestBody FoodItem foodItem) {
        Optional<Menu> menuOptional = menuService.getMenuById(menuId);
        menuOptional.ifPresent(menu -> {
            menu.getFoodItems().add(foodItem);
            menuService.updateMenu(menu);
        });
        return menuOptional.orElse(null);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PostMapping(value = {"/{menuId}/addNewFoodItem"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Menu addNewFoodItem(@PathVariable Integer menuId,
                                    @RequestPart("foodItem") FoodItem foodItem,
                                   @RequestPart("imageFile") MultipartFile[] file) {
        Optional<Menu> menuOptional=menuService.getMenuById(menuId);
        menuOptional.ifPresent(menu -> {
            menu.getFoodItems().add(foodItem);
        try {
            String username = JwtRequestFilter.CURRENT_USER;

            User user = null;
            if(username != null) {
                user = userRepository.findById(username).get();
            }
            foodItem.setUser(user);

                Set<ImageModel> images = uploadImage(file);
                foodItem.setFoodItemImages(images);
//                foodItemService.addNewFoodItem(foodItem);
                menuService.updateMenu(menu);
            } catch (Exception e) {
            System.out.println(e.getMessage());
//            return null;
        }

        });

        return menuOptional.orElse(null);

//            return foodItemService.addNewFoodItem(foodItem);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @PutMapping("/{menuId}/fooditems/{foodItemId}")
    public Menu updateFoodItemInMenu(
            @PathVariable Integer menuId,
            @PathVariable Integer foodItemId,
            @RequestBody FoodItem foodItem) {
        Optional<Menu> menuOptional = menuService.getMenuById(menuId);
        menuOptional.ifPresent(menu -> {
            Optional<FoodItem> foodItemOptional = menu.getFoodItems().stream()
                    .filter(item -> item.getFoodItemId().equals(foodItemId))
                    .findFirst();
            foodItemOptional.ifPresent(item -> {
                item.setFoodItemName(foodItem.getFoodItemName());////if error change set/getFoodItemName to set/getName
                item.setFoodItemPrice(foodItem.getFoodItemPrice());////here too
                menuService.updateMenu(menu);
            });
        });
        return menuOptional.orElse(null);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @DeleteMapping("/{menuId}/fooditems/{foodItemId}")
    public void removeFoodItemFromMenu(@PathVariable Integer menuId, @PathVariable Integer foodItemId) {
        Optional<Menu> menuOptional = menuService.getMenuById(menuId);
        menuOptional.ifPresent(menu -> {
            menu.getFoodItems().removeIf(foodItem -> foodItem.getFoodItemId().equals(foodItemId));
            menuService.updateMenu(menu);
        });
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
}
