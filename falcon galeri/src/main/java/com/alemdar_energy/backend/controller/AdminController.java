package com.alemdar_energy.backend.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alemdar_energy.backend.model.Order;
import com.alemdar_energy.backend.service.OrderService;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private OrderService orderService;

  @Autowired
  public AdminController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/showPanel")
  public String showAdminPanel(Model model) {
    return "admin/admin-panel";
  }

 @GetMapping("/orders")
public String showOrders(Model model) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    List<Order> orders = orderService.getAllOrders().stream().map(order -> {
        order.setFormattedDate(order.getOrderDate().format(formatter));
        return order;
    }).toList();

    model.addAttribute("orders", orders);
    return "admin/order-list";
}


}
