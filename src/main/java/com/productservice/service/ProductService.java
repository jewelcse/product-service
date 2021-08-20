package com.productservice.service;

import com.productservice.entity.Product;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    void init();
    void save(MultipartFile file);
    String saveFiles(MultipartFile file);
    Resource load(String filename);
    List<Product> getProducts();
    Optional<Product> getProductById(int productId);
    Product saveOrUpdateProduct(Product product);
    void removeProduct(Product product);
    void removeProduct(int productId);
    boolean getProductByProductSlug(String ps);
    List<Product> getCategoryProducts(int categoryId);


}
