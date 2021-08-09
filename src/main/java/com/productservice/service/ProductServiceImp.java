package com.productservice.service;

import com.productservice.entity.Product;
import com.productservice.repository.ProductRepository;
import com.productservice.util.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImp  implements ProductService{


    private ProductRepository productRepository;
    private CachingService cachingService;

    @Autowired
    public ProductServiceImp(ProductRepository repository,CachingService service){
        this.productRepository = repository;
        this.cachingService = service;
    }


    @Override
    public Product saveOrUpdateProduct(Product product) {
        cachingService.evictAllCaches();
        return productRepository.save(product);
    }

    @Override
    public void removeProduct(Product product){
         productRepository.delete(product);
         cachingService.evictAllCaches();
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
