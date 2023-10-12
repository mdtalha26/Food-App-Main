package com.fooddelivery.zoomato.controller;

import com.fooddelivery.zoomato.entity.Restaurant;
import com.fooddelivery.zoomato.service.UserService;
import com.fooddelivery.zoomato.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }

    @PostMapping({"/registerNewUser"})
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody User user){
        try {
            return ResponseEntity.ok(userService.registerNewUser(user));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping({"/registerNewRestaurant"})
    public ResponseEntity<?> registerNewRestaurant(@Valid @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.registerNewRestaurant(user));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('Admin')")
    public User forAdmin(){
        return this.userService.getMyDetails();
    }

    @GetMapping({"/forRestaurant"})
    @PreAuthorize("hasRole('Restaurant')")
    public User forRestaurant(){
        return this.userService.getMyDetails();
    }

    @GetMapping({"/forUser"})
    @PreAuthorize("hasRole('User')")
    public User forUser(){
        return this.userService.getMyDetails();
    }

    @PutMapping({"/updateDetails"})
    @PreAuthorize("hasRole('User')")
    public User updateDetails(@RequestBody User user){
        return this.userService.updateUserDetails(user);
    }
}
