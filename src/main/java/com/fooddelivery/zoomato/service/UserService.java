package com.fooddelivery.zoomato.service;


import com.fooddelivery.zoomato.entity.Restaurant;
import com.fooddelivery.zoomato.entity.User;



public interface UserService {


    public void initRoleAndUser();

    public User registerNewUser(User user);


    public User registerNewRestaurant(User user);

    public String getEncodedPassword(String password);

    public User getMyDetails();
}
