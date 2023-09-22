package com.fooddelivery.zoomato.repository;

import com.fooddelivery.zoomato.entity.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends CrudRepository<FoodItem, Integer> {//changes from JpaRepo to CrudRepo cos findAll clash
    public List<FoodItem> findAll(Pageable pageable);

    public List<FoodItem> findByFoodItemNameContainingIgnoreCaseOrFoodItemDescriptionContainingIgnoreCase(
            String key1, String key2, Pageable pageable
    );
}
