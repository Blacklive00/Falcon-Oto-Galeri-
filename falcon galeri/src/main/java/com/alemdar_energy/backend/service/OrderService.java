package com.alemdar_energy.backend.service;

import java.util.List;

import com.alemdar_energy.backend.model.Order;

public interface OrderService {
    void saveOrder(Order order);
    List<Order> getAllOrders();
}
