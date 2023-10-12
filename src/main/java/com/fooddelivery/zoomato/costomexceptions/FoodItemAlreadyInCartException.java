package com.fooddelivery.zoomato.costomexceptions;

public class FoodItemAlreadyInCartException extends RuntimeException{

    public FoodItemAlreadyInCartException(String message) {
        super(message);
    }
}
