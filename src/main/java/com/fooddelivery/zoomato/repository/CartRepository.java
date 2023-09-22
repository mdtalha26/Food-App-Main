package com.fooddelivery.zoomato.repository;

import com.fooddelivery.zoomato.entity.Cart;
import com.fooddelivery.zoomato.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer > {
    public List<Cart> findByUser(User user);
}
