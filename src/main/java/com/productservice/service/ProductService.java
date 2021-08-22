package com.productservice.service;

import com.productservice.dto.ProductDto;
import com.productservice.entity.Product;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    void init();
    void save(MultipartFile file);
    String saveFiles(MultipartFile file);
    Resource load(String filename);
    List<Product> getProducts();
    List<Product> getProductsWithSorting(String field);
    Page<Product> getProductsWithPagination(int offset, int pageSize);
    //Page<Product> getProductsWithPaginationAndSorting(int offset,int pageSize,String field);
    Optional<Product> getProductById(int productId);
    Product saveOrUpdateProduct(ProductDto product) throws IOException;
    void removeProduct(Product product);
    void removeProduct(int productId);
    boolean getProductByProductSlug(String ps);
    List<Product> getCategoryProducts(int categoryId);


}
