package com.fooddelivery.zoomato.repository;

import com.fooddelivery.zoomato.entity.Cart;
import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.Restaurant;
import com.fooddelivery.zoomato.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant,Integer> {
    public List<Restaurant> findByUser(User user);

    public List<Restaurant> findAll(Pageable pageable);

    public Optional<Restaurant> findByFssaiLicenseNumber(String fssaiLicenseNumber);

    public List<Restaurant> findByRestaurantNameContainingIgnoreCaseOrRestaurantAddressContainingIgnoreCase(
            String key1, String key2, Pageable pageable
    );
}
