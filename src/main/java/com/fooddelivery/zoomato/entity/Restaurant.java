package com.fooddelivery.zoomato.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantId;

    private String restaurantName;

    private String fssaiLicenseNumber;

    private String panCard;



    @OneToOne
    private User user;

    private String restaurantAddress;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "restaurant_documents",
            joinColumns = {
                    @JoinColumn(name = "restaurant_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            }
    )
    private Set<ImageModel> documents;

    // Other attributes and annotations


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    private List<Menu> menus = new ArrayList<>();

    private String status;

    // Getter and Setter methods

   public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }


    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getFssaiLicenseNumber() {
        return fssaiLicenseNumber;
    }

    public void setFssaiLicenseNumber(String fssaiLicenseNumber) {
        this.fssaiLicenseNumber = fssaiLicenseNumber;
    }

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
    }

    public Set<ImageModel> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<ImageModel> documents) {
        this.documents = documents;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //       other methods


    public Restaurant() {
    }

    public Restaurant(Integer restaurantId, String restaurantName, List<Menu> menus) {
        //this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
               // "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", menus=" + menus +
                '}';
    }

    public Restaurant(String restaurantName, User user, String restaurantAddress, Set<ImageModel> documents, List<Menu> menus) {
        this.restaurantName = restaurantName;
        this.user = user;
        this.restaurantAddress = restaurantAddress;
        this.documents = documents;
        this.menus = menus;
    }
}