package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.entity.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.fooddelivery.zoomato.repository.CartRepository;
import com.fooddelivery.zoomato.repository.OrderDetailRepository;
import com.fooddelivery.zoomato.repository.FoodItemRepository;
import com.fooddelivery.zoomato.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface OrderDetailService {



    public List<OrderDetail> getAllOrderDetails(String status,String sort,int pageNumber, String searchKey);

    public List<OrderDetail> getAllOrderDetailsForRestaurant(String status,String sort,int pageNumber,String searchKey);

    public List<OrderDetail> getOrderDetails();

    public List<OrderDetail> getOrderDetailsForRestaurant();

    public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout);

    public void markOrderAsDelivered(Integer orderId);

    public TransactionDetails createTransaction(Double amount);

    public TransactionDetails prepareTransactionDetails(Order order);
}
