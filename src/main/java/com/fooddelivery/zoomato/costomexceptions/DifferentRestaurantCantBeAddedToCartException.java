package com.fooddelivery.zoomato.costomexceptions;

public class DifferentRestaurantCantBeAddedToCartException extends RuntimeException{

    public DifferentRestaurantCantBeAddedToCartException(String message) {
        super(message);
    }
}
