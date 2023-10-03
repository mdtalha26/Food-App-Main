package com.fooddelivery.zoomato.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer foodItemId;
    @NotBlank(message = "Food Item name cannot be blank")
    private String foodItemName;
    @Column(length = 2000)
    @NotBlank(message = "Food Item description cannot be blank")
    private String foodItemDescription;
    @NotBlank(message = "FoodItem Category required")
    private String foodItemCategory;
    @NotNull
    private Double foodItemPrice;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "foodItem_images",
            joinColumns = {
                    @JoinColumn(name = "foodItem_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            }
    )
    private Set<ImageModel> foodItemImages;

    @OneToOne
    private User user;

    public FoodItem() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FoodItem(Integer foodItemId, String foodItemName, String foodItemDescription, String foodItemCategory, Double foodItemPrice, Set<ImageModel> foodItemImages,User user) {
        this.foodItemId = foodItemId;
        this.foodItemName = foodItemName;
        this.foodItemDescription = foodItemDescription;
        this.foodItemCategory=foodItemCategory;
        this.foodItemPrice = foodItemPrice;
//        this.foodItemDiscountedPrice = foodItemDiscountedPrice;
//        this.foodItemActualPrice = foodItemActualPrice;
        this.foodItemImages = foodItemImages;
        this.user = user;
    }

    public Integer getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Integer foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public String getFoodItemDescription() {
        return foodItemDescription;
    }

    public void setFoodItemDescription(String foodItemDescription) {
        this.foodItemDescription = foodItemDescription;
    }

    public String getFoodItemCategory() {
        return foodItemCategory;
    }

    public void setFoodItemCategory(String foodItemCategory) {
        this.foodItemCategory = foodItemCategory;
    }

    public Double getFoodItemPrice() {
        return foodItemPrice;
    }

    public void setFoodItemPrice(Double foodItemPrice) {
        this.foodItemPrice = foodItemPrice;
    }

    public Set<ImageModel> getFoodItemImages() {
        return foodItemImages;
    }

    public void setFoodItemImages(Set<ImageModel> foodItemImages) {
        this.foodItemImages = foodItemImages;
    }
}
