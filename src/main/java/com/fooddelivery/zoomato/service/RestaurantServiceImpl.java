package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.costomexceptions.FssaiNumberAlreadyExistsException;
import com.fooddelivery.zoomato.entity.*;
import com.fooddelivery.zoomato.repository.MenuRepository;
import com.fooddelivery.zoomato.repository.RestaurantRepository;
import com.fooddelivery.zoomato.repository.RoleRepository;
import com.fooddelivery.zoomato.repository.UserRepository;
import com.fooddelivery.zoomato.utility.GlobalResources;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private RestaurantRepository restaurantRepository;
    private MenuRepository menuRepository;



    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }
    private Logger logger= GlobalResources.getLogger(FoodItemServiceImpl.class);

//    public List<Restaurant> getAllRestaurants() {
//        String methodName="getAllRestaurants()";
//        logger.info(methodName+"Called");
//        return restaurantRepository.findAll();
//    }


    public List<Restaurant> getAllRestaurants(int pageNumber, String searchKey) {
        Pageable pageable = PageRequest.of(pageNumber,12);

        if(searchKey.equals("")) {
            return (List<Restaurant>) restaurantRepository.findAll(pageable);
        } else {
            return (List<Restaurant>) restaurantRepository.findByRestaurantNameContainingIgnoreCaseOrRestaurantAddressContainingIgnoreCase(
                    searchKey, searchKey, pageable
            );
        }

    }

    public Optional<Restaurant> getRestaurantById(Integer restaurantId) {
        String methodName="getRestaurantById()";
        logger.info(methodName+"Called");
        return restaurantRepository.findById(restaurantId);
    }

    public List<Restaurant> getMyRestaurants() {
        String username = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(username).get();
        return restaurantRepository.findByUser(user);
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        String username = JwtRequestFilter.CURRENT_USER;
        Optional<Restaurant> existingRestaurant=restaurantRepository.findByFssaiLicenseNumber(restaurant.getFssaiLicenseNumber());
        Boolean restIsPresent=existingRestaurant.isPresent();
        if(restIsPresent==true){
            throw new FssaiNumberAlreadyExistsException("FSSAI Number is already linked with a restaurant, Please login or provide a different FSSAI license");
        }else {

            User user = null;
            if (username != null) {
                user = userRepository.findById(username).get();
            }
            restaurant.setUser(user);
            String methodName = "createRestaurant()";
            logger.info(methodName + "Called");
            return restaurantRepository.save(restaurant);
//        return "Menu created Successfully";
        }
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        String methodName="updateRestaurant()";
        logger.info(methodName+"Called");
        return restaurantRepository.save(restaurant);
//        return "Menu updated successfully";
    }

    public void deleteRestaurant(Integer restaurantId) {
        String methodName="deleteRestaurant()";
        logger.info(methodName+"Called");
        restaurantRepository.deleteById(restaurantId);
//        return "Menu deleted successfully";
    }

    public void deleteAllRestaurants(){
        restaurantRepository.deleteAll();

    }

    public List<Menu> getAllMenus() {
        String methodName="getAllMenus()";
        logger.info(methodName+"Called");
        return menuRepository.findAll();
    }

    public Optional<Menu> getMenuById(Integer menuId) {
        String methodName="getMenuById()";
        logger.info(methodName+"Called");
        return menuRepository.findById(menuId);
    }

    public Menu createMenu(Menu menu) {
        String methodName="createMenu()";
        logger.info(methodName+"Called");
        return menuRepository.save(menu);
//        return "Menu created successfully";
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



}