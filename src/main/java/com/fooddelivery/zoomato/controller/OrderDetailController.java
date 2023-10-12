package com.fooddelivery.zoomato.controller;

import com.fooddelivery.zoomato.entity.OrderDetail;
import com.fooddelivery.zoomato.entity.OrderInput;
import com.fooddelivery.zoomato.entity.TransactionDetails;
import com.fooddelivery.zoomato.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @PreAuthorize("hasRole('User')")
    @PostMapping({"/placeOrder/{isSingleProductCheckout}"})
    public void placeOrder(@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout,
            @RequestBody OrderInput orderInput) {
        orderDetailService.placeOrder(orderInput, isSingleProductCheckout);
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getOrderDetails"})
    public List<OrderDetail> getOrderDetails(@RequestParam(defaultValue = "0") int pageNumber,
                                             @RequestParam(defaultValue = "") String searchKey) {
        return orderDetailService.getOrderDetails(pageNumber, searchKey);
    }

    @PreAuthorize("hasRole('Restaurant')")
    @GetMapping({"/getRestaurantOrderDetails/{status}/{sort}"})
    public List<OrderDetail> getOrderDetailsForRestaurant(@PathVariable(name="status") String status,
                                                          @PathVariable(name="sort") String sort,
                                                          @RequestParam(defaultValue = "0") int pageNumber,
                                                          @RequestParam(defaultValue = "") String searchKey) {
        return orderDetailService.getAllOrderDetailsForRestaurant(status,sort,pageNumber, searchKey);
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping({"/getAllOrderDetails/{status}/{sort}"})
    public List<OrderDetail> getAllOrderDetails(@PathVariable(name = "status") String status,
                                                @PathVariable(name="sort") String sort,
                                                @RequestParam(defaultValue = "0") int pageNumber,
                                                @RequestParam(defaultValue = "") String searchKey) {
        return orderDetailService.getAllOrderDetails(status,sort,pageNumber, searchKey);
    }

    @PreAuthorize("hasAnyRole('Admin', 'Restaurant')")
    @GetMapping({"/markOrderAsDelivered/{orderId}"})
    public void markOrderAsDelivered(@PathVariable(name = "orderId") Integer orderId) {
        orderDetailService.markOrderAsDelivered(orderId);
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/createTransaction/{amount}"})
    public TransactionDetails createTransaction(@PathVariable(name = "amount") Double amount) {
        return orderDetailService.createTransaction(amount);
    }
}
