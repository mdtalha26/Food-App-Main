package com.fooddelivery.zoomato.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;
    @NotBlank(message = "Menu name cannot be blank")
    private String menuName;
    @NotBlank(message = "Menu Description cannot be blank")
    private String menuDescription;
    // Other attributes and annotations

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<FoodItem> foodItems = new ArrayList<>();

    // Getter and Setter methods

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }


    // Other methods


    public Menu() {
    }

    public Menu(Integer menuId, String menuName, List<FoodItem> foodItems) {
        this.menuId = menuId;//////////////////////////////if error, remove this.id and use super();
        this.menuName = menuName;
        this.foodItems = foodItems;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + menuId +
                ", name='" + menuName + '\'' +
                ", foodItems=" + foodItems +
                '}';
    }
}
