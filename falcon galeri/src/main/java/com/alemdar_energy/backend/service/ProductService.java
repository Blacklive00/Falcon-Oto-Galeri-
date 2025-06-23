package com.alemdar_energy.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alemdar_energy.backend.model.Product;
import com.alemdar_energy.backend.repository.ProductRepo;

@Service
public class ProductService {

  private ProductRepo productRepo;

  @Autowired
  public ProductService(ProductRepo productRepo) {
    this.productRepo = productRepo;
  }

  public Product getById(Long id){
    return productRepo.findById(id).get();
  }

  public List<Product> getAllData() {
    return productRepo.findAll();
  }

  public Product findProductById(Long id) {
    Product tempProduct = productRepo.findById(id).get();
    return tempProduct;
  }

  public Product updateProduct(Long id, Product product) {
    Product theProduct = productRepo.findById(id).get();
    theProduct.setProductName(product.getProductName());
    theProduct.setStock(product.getStock());
    theProduct.setPrice(product.getPrice());
    theProduct.setUrl(product.getUrl());
    productRepo.save(theProduct);
    return theProduct;
  }

  public void deleteProduct(Long id) {
    productRepo.deleteById(id);
  }

  public Product createProduct(Product theProduct) {
    Product tempProduct = theProduct;
    productRepo.save(theProduct);
    return tempProduct;

  }
}
