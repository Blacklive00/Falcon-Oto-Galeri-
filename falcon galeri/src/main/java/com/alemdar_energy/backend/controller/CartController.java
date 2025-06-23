package com.alemdar_energy.backend.controller;

import com.alemdar_energy.backend.model.Cart;
import com.alemdar_energy.backend.model.CartItem;
import com.alemdar_energy.backend.model.Order;
import com.alemdar_energy.backend.model.OrderItem;
import com.alemdar_energy.backend.model.User;
import com.alemdar_energy.backend.service.CartService;
import com.alemdar_energy.backend.service.OrderService;
import com.alemdar_energy.backend.service.ProductService;
import com.alemdar_energy.backend.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;
  private final UserService userService;
  private final OrderService orderService;
  private final ProductService productService;

  @Autowired
  public CartController(CartService cartService, UserService userService, OrderService orderService,
      ProductService productService) {
    this.cartService = cartService;
    this.userService = userService;
    this.orderService = orderService;
    this.productService = productService;
  }

  // Test kullanıcı doğrulama
  @GetMapping("/testUser")
  public String testUser(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      return "redirect:/login"; // Giriş yapmamışsa login sayfasına yönlendir
    }
    return "redirect:/cart/"; // Eğer kullanıcı giriş yapmışsa sepete yönlendir
  }

  // Kullanıcının sepetini al ve göster
  @GetMapping("/")
  public String showCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {

    if (userDetails == null) {
      return "redirect:/login"; // Giriş yapmamışsa login sayfasına yönlendir
    }

    User user = userService.findByUsername(userDetails.getUsername());
    if (user == null) {
      model.addAttribute("error", "Kullanıcı bulunamadı.");
      return "error"; // Hata sayfasına yönlendir
    }

    // Kullanıcının sepetini al
    Cart cart = cartService.getCartByUser(user);

    // Toplam fiyatı hesaplamak için getTotalPrice metodunu kullanıyoruz
    double totalPrice = cart.getTotalPrice();

    model.addAttribute("cart", cart);
    model.addAttribute("totalPrice", totalPrice); // Toplam fiyatı modelde ekliyoruz

    return "products/cart"; // Sepet sayfasını göster
  }

  // Sepete ürün ekleme işlemi
  @GetMapping("/add")
  public String addToCart(@AuthenticationPrincipal UserDetails userDetails,
      @RequestParam Long productId,
      @RequestParam int quantity,
      Model model) {
    if (userDetails == null) {
      return "redirect:/login"; // Giriş yapmamışsa login sayfasına yönlendir
    }

    User theUser = userService.findByUsername(userDetails.getUsername());
    if (theUser == null) {
      model.addAttribute("error", "Kullanıcı bulunamadı.");
      return "error"; // Hata sayfasına yönlendir
    }

    // Sepete ürün ekleme işlemi
    try {
      cartService.addItemToCart(theUser, productId, quantity);
    } catch (Exception e) {
      model.addAttribute("error", "Ürün eklenirken bir hata oluştu: " + e.getMessage());
      return "error"; // Hata sayfasına yönlendir
    }

    return "redirect:/cart/"; // Sepet sayfasına yönlendir
  }

  // Sepetten ürün çıkarma işlemi
  @PostMapping("/remove")
  public String removeFromCart(@AuthenticationPrincipal UserDetails user,
      @RequestParam Long productId,
      Model model) {
    User tempUser = userService.findByUsername(user.getUsername());
    System.out.println("REmove methodundaki user  : " + user);
    if (tempUser == null) {
      // Kullanıcı giriş yapmamışsa login sayfasına yönlendir
      return "redirect:/login"; // Hata sayfasına gitmek yerine login sayfasına yönlendirelim
    }

    try {
      cartService.removeItemFromCart(tempUser, productId);
    } catch (Exception e) {
      model.addAttribute("error", "Ürün çıkarılırken bir hata oluştu: " + e.getMessage());
      return "error"; // Hata sayfasına yönlendir
    }

    return "redirect:/cart/"; // Sepet sayfasına yönlendir
  }

  // Sepeti temizleme işlemi
  @PostMapping("/clear")
  public String clearCart(@AuthenticationPrincipal UserDetails user, Model model) {

    User tempUser = userService.findByUsername(user.getUsername());

    if (tempUser == null) {
      System.out.println(user);
      // Kullanıcı giriş yapmamışsa login sayfasına yönlendir
      return "redirect:/login"; // Hata sayfasına gitmek yerine login sayfasına yönlendirelim
    }

    try {
      cartService.clearCart(tempUser);
    } catch (Exception e) {
      model.addAttribute("error", "Sepet temizlenirken bir hata oluştu: " + e.getMessage());
      return "error"; // Hata sayfasına yönlendir
    }

    return "redirect:/cart/"; // Sepet sayfasına yönlendir
  }

  @PostMapping("/order")
  public String CartToOrder(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    User user = userService.findByUsername(userDetails.getUsername());
    if (user == null)
      return "redirect:/login";

    Cart cart = cartService.getCartByUser(user);
    if (cart.getItems().isEmpty())
      return "redirect:/cart/";

    // Siparişi oluştur
    Order order = new Order();
    order.setUser(user);
    order.setOrderDate(LocalDateTime.now());

    List<OrderItem> orderItems = new ArrayList<>();

    Double totalPrice = 0.0;
    for (CartItem item : cart.getItems()) {
      OrderItem orderItem = new OrderItem();
      orderItem.setProduct(item.getProduct());
      orderItem.setQuantity(item.getQuantity());
      orderItem.setPrice(item.getProduct().getPrice());
      orderItem.setOrder(order);
      orderItems.add(orderItem);

      // item.getProduct().setStock(item.getProduct().getStock()-orderItem.getQuantity());

      totalPrice += item.getProduct().getPrice() * item.getQuantity();

      // stoktan düş
      // productService.updateProduct(item.getProduct().getId(), item.getProduct());

      int currentStock = item.getProduct().getStock();
      int quantity = orderItem.getQuantity();
      System.out.println("||||||||||||||||Current Stock : "+currentStock);

      System.out.println("|||||||||||||||||| Quantity : "+ quantity);

      System.out.println("|||||||||||||||| item id : "+item.getProduct().getId());
      if (currentStock < quantity) {
        // Stok yetersiz, hata döndür veya kullanıcıyı bilgilendir
        throw new IllegalStateException("Stok yetersiz: " + item.getProduct().getProductName());
      }

      item.getProduct().setStock(currentStock - quantity);
      productService.updateProduct(item.getProduct().getId(), item.getProduct());

    }

    order.setItems(orderItems);
    order.setTotalPrice(totalPrice);

    // Siparişi kaydet
    orderService.saveOrder(order);

    // Sepeti temizle
    cartService.clearCart(user);

    return "redirect:/cart/";
  }

}
