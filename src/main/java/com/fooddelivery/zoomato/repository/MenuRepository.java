package com.fooddelivery.zoomato.repository;

import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.Menu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository <Menu,Integer> {

    @Query("SELECT mi.foodItems FROM Menu mi WHERE mi.menuId = :menuId")
    List<FoodItem> findFoodItemByMenuId(@Param("menuId") Integer menuId, Pageable pageable);

    //@Query("SELECT mi.foodItems FROM Menu mi WHERE mi.menuId = :menuId AND LOWER(mi.foodItems.foodItemName) LIKE %:searchKey%")
    @Query("SELECT DISTINCT fi FROM Menu m JOIN m.foodItems fi WHERE m.menuId = :menuId AND LOWER(fi.foodItemName) LIKE %:searchKey%")
    List<FoodItem> findFilteredFoodItemsByMenuIdAndSearchKey(
            @Param("menuId") Integer menuId,
            @Param("searchKey") String searchKey,
            Pageable pageable
    );
}
