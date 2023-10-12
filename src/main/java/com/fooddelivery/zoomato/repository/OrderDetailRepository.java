package com.fooddelivery.zoomato.repository;

import com.fooddelivery.zoomato.entity.FoodItem;
import com.fooddelivery.zoomato.entity.OrderDetail;
import com.fooddelivery.zoomato.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    public List<OrderDetail> findAllByOrderByOrderCreatedAtAsc(Pageable pageable);

    public List<OrderDetail> findAllByOrderByOrderCreatedAtDesc(Pageable pageable);
    public List<OrderDetail> findByUser(User user);

    public List<OrderDetail> findByRestaurant(User restaurant);

    public List<OrderDetail> findByRestaurantOrderByOrderCreatedAtAsc(User restaurant);

    public List<OrderDetail> findByRestaurantOrderByOrderCreatedAtDesc(User restaurant);

    public List<OrderDetail> findByOrderStatusOrderByOrderCreatedAtAsc(Pageable pageable,String status);

    public List<OrderDetail> findByOrderStatusOrderByOrderCreatedAtDesc(Pageable pageable,String status);

    public List<OrderDetail> findByRestaurantAndOrderStatus(User restaurant,String status);

    List<OrderDetail> findByRestaurantAndOrderStatusOrderByOrderCreatedAtDesc(User restaurant, String status);

    List<OrderDetail> findByRestaurantAndOrderStatusOrderByOrderCreatedAtAsc(User restaurant, String status);

    public List<OrderDetail> findByOrderFullNameContainingIgnoreCase(
            String key1, Pageable pageable
            );

    public List<OrderDetail> findByRestaurantAndOrderFullNameContainingIgnoreCase(
           User restaurant, String key1,Pageable pageable
    );

    public List<OrderDetail> findByUserAndTransactionIdContainingIgnoreCase(
            User user, String key1, Pageable pageable
    );
}
