package com.fooddelivery.zoomato.service;

import com.fooddelivery.zoomato.configuration.JwtRequestFilter;
import com.fooddelivery.zoomato.entity.*;
import com.fooddelivery.zoomato.repository.CartRepository;
import com.fooddelivery.zoomato.repository.FoodItemRepository;
import com.fooddelivery.zoomato.repository.OrderDetailRepository;
import com.fooddelivery.zoomato.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private static final String ORDER_PLACED = "Placed";

    private static final String KEY = "rzp_test_AXBzvN2fkD4ESK";
    private static final String KEY_SECRET = "bsZmiVD7p1GMo6hAWiy4SHSH";
    private static final String CURRENCY = "INR";

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    public List<OrderDetail> getAllOrderDetails(String status,String sort,int pageNumber, String searchKey) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber,12);
        if(searchKey.equals("")) {
            if(sort.equals("Desc")) {
                if (status.equals("All")) {
                    orderDetailRepository.findAllByOrderByOrderCreatedAtDesc(pageable).forEach(
                            x -> orderDetails.add(x)
                    );
                } else {
                    orderDetailRepository.findByOrderStatusOrderByOrderCreatedAtDesc(pageable,status).forEach(
                            x -> orderDetails.add(x)
                    );
                }
            }else {
                if (status.equals("All")) {
                    orderDetailRepository.findAllByOrderByOrderCreatedAtAsc(pageable).forEach(
                            x -> orderDetails.add(x)
                    );
                } else {
                    orderDetailRepository.findByOrderStatusOrderByOrderCreatedAtAsc(pageable,status).forEach(
                            x -> orderDetails.add(x)
                    );
                }
            }
        } else {
            orderDetailRepository.findByOrderFullNameContainingIgnoreCase(
                    searchKey, pageable
            ).forEach(x ->orderDetails.add(x));
        }




        return orderDetails;
    }

    public List<OrderDetail> getAllOrderDetailsForRestaurant(String status, String sort,int pageNumber, String searchKey) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber,12);

        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(currentUser).get();

        if(searchKey.equals("")) {
            if (sort.equals("Desc")) {
                if (status.equals("All")) {
                    orderDetailRepository.findByRestaurantOrderByOrderCreatedAtDesc(user).forEach(
                            x -> orderDetails.add(x)
                    );
                } else {
                    orderDetailRepository.findByRestaurantAndOrderStatusOrderByOrderCreatedAtDesc(user, status).forEach(
                            x -> orderDetails.add(x)
                    );
                }
            } else {
                if (status.equals("All")) {
                    orderDetailRepository.findByRestaurantOrderByOrderCreatedAtAsc(user).forEach(
                            x -> orderDetails.add(x)
                    );
                } else {
                    orderDetailRepository.findByRestaurantAndOrderStatusOrderByOrderCreatedAtAsc(user, status).forEach(
                            x -> orderDetails.add(x)
                    );
                }
            }
        }else {
            orderDetailRepository.findByRestaurantAndOrderFullNameContainingIgnoreCase(
                    user,searchKey, pageable
            ).forEach(x ->orderDetails.add(x));
        }


        return orderDetails;
    }

    public List<OrderDetail> getOrderDetails(int pageNumber, String searchKey ) {
        Pageable pageable = PageRequest.of(pageNumber,12);
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(currentUser).get();

        return orderDetailRepository.findByUserAndTransactionIdContainingIgnoreCase(user,searchKey,pageable);
    }

    public List<OrderDetail> getOrderDetailsForRestaurant() {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepository.findById(currentUser).get();

        return orderDetailRepository.findByRestaurant(user);
    }

    public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout) {
        List<OrderFoodItemQuantity> foodItemQuantityList = orderInput.getOrderFoodItemQuantityList();

        for (OrderFoodItemQuantity o: foodItemQuantityList) {
            FoodItem foodItem = foodItemRepository.findById(o.getFoodItemId()).get();

            User restaurant = foodItem.getUser();

            String currentUser = JwtRequestFilter.CURRENT_USER;
            User user = userRepository.findById(currentUser).get();

            OrderDetail orderDetail = new OrderDetail(
                    orderInput.getFullName(),
                    orderInput.getFullAddress(),
                    orderInput.getContactNumber(),
                    orderInput.getAlternateContactNumber(),
                    ORDER_PLACED,
                    foodItem.getFoodItemPrice() * o.getQuantity(),
                    foodItem,
                    user,
                    orderInput.getTransactionId(),
                    restaurant
            );

            // empty the cart.
            if(!isSingleProductCheckout) {
                List<Cart> carts = cartRepository.findByUser(user);
                carts.stream().forEach(x -> cartRepository.deleteById(x.getCartId()));
            }

            orderDetailRepository.save(orderDetail);
        }
    }

    public void markOrderAsDelivered(Integer orderId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderId).get();

        if(orderDetail != null) {
            orderDetail.setOrderStatus("Delivered");
            orderDetailRepository.save(orderDetail);
        }

    }

    public TransactionDetails createTransaction(Double amount) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", (amount * 100) );
            jsonObject.put("currency", CURRENCY);

            RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);

            Order order = razorpayClient.orders.create(jsonObject);

            TransactionDetails transactionDetails =  prepareTransactionDetails(order);
            return transactionDetails;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public TransactionDetails prepareTransactionDetails(Order order) {
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");

        TransactionDetails transactionDetails = new TransactionDetails(orderId, currency, amount, KEY);
        return transactionDetails;
    }
}
