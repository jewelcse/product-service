package com.productservice.service;

import com.productservice.entity.Product;
import com.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImp  implements ProductService{


    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImp(ProductRepository repository){
        this.productRepository = repository;
    }


    @Override
    public Product saveOrUpdateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void removeProduct(Product product){
         productRepository.delete(product);
    }

    @Override
    public boolean getProductByProductSlug(String ps) {
        Optional<Product> product =  productRepository.findByProductSlug(ps);

        if (!product.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public List<Product> getProducts() {
        System.out.println("Fetching Data from Database");
        return  productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(int productId) {
        return productRepository.findById(productId);
    }
}
