package com.productservice.service;

import com.productservice.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getProducts();
    Optional<Product> getProductById(int productId);
    Product saveOrUpdateProduct(Product product);
    void removeProduct(Product product);

    boolean getProductByProductSlug(String ps);
}
