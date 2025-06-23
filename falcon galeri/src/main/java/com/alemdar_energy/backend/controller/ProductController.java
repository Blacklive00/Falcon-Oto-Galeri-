package com.alemdar_energy.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alemdar_energy.backend.model.Product;
import com.alemdar_energy.backend.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

  private ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/list")
  public String showProducts(Model model) {
    List<Product> products = productService.getAllData();
    model.addAttribute("products", products);
    return "products/products-list";
  }

  @GetMapping("/showFormForAdd")
  public String showFormForAdd(Model model) {
    Product product = new Product();
    model.addAttribute("product", product);
    return "products/add-form";
  }

  @PostMapping("/save")
  public String saveProduct(@ModelAttribute("product") Product product) {
    productService.createProduct(product);
    return "redirect:/products/list";
  }

  @GetMapping("/showFormForUpdate")
  public String showFormForUpdate(@RequestParam("productId") Long id, Model model) {
    Product product = productService.findProductById(id);
    model.addAttribute("theProduct", product);
    return "products/update-form";
  }

  @GetMapping("/deleteProduct")
  public String deleteProduct(@RequestParam("productId") Long id) {
    productService.deleteProduct(id);
    return "redirect:/products/list";
  }

  @GetMapping("/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "products/product-detail"; // product-detail.html Thymeleaf sayfası döndürülecek
    }

}
