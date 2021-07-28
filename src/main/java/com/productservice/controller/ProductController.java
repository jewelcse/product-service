package com.productservice.controller;

import com.productservice.entity.Product;
import com.productservice.exception.ApplicationException;
import com.productservice.exception.ProductCustomExceptionHandler;
import com.productservice.exception.ProductNotFoundException;
import com.productservice.service.ProductServiceImp;
import com.productservice.util.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductServiceImp productService;

    @Autowired
    public ProductController(ProductServiceImp productService){
        this.productService = productService;
    }


    //@RabbitListener(queues = ProductConfig.PRODUCT_QUEUE)
    @PostMapping("/product/create")
    public ResponseEntity <Product> createProduct(@RequestBody Product product) throws RuntimeException{

        if (product.getProductTitle().isEmpty() || product.getProductTitle() == null){
            throw new ApplicationException("Product Title can't be Empty");
        }

        if (product.getProductDescription().isEmpty() || product.getProductDescription() == null){
            throw new ApplicationException("Product Description can't be Empty");
        }

        if (product.getProductOverview().isEmpty() || product.getProductOverview() == null){
            throw new ApplicationException("Product Overview can't be Empty");
        }

        if (product.getProductImages().length==0){
            throw new ApplicationException("Add at least one Image");
        }

        if (product.getProductOriginalPrice()<0){
            throw new ApplicationException("Product Price can't be Empty");
        }

        if (product.getDiscountPercentage()> 0){
            double disAmount = (product.getProductOriginalPrice()* product.getDiscountPercentage())/100;
            product.setProductFinalPrice(product.getProductOriginalPrice()-disAmount);
        }else{
            product.setProductFinalPrice(product.getProductOriginalPrice());
        }

        String ps = MethodUtils.toSlug(product.getProductTitle());
        boolean doesExit = productService.getProductByProductSlug(ps);
        if (doesExit){
            throw new ApplicationException(product.getProductTitle() + "Already Exit ");
        }

        product.setProductSlug(ps);
        product.setDate(MethodUtils.getDate());

        System.out.println(product);

        return new ResponseEntity<>(productService.saveOrUpdateProduct(product), HttpStatus.CREATED);
    }
    /*
     Admin Endpoint for remove product
     */
    @GetMapping("/remove/product")
    public ResponseEntity<String> removeProduct(@RequestParam int id){

        if (id<0 ){
            throw new ApplicationException("Invalid Product Id!");
        }

        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for this id :: " + id));

        productService.removeProduct(product);

        return new ResponseEntity<>("Deleted product successfully!",HttpStatus.OK);

    }

    /*
    User and Admin endpoint for get all product list
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/get/products")
    @Cacheable(value = "products")
    public ResponseEntity<List<Product>> getAllProduct(){
        return new ResponseEntity<>(productService.getProducts(),HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/get/product")
    public ResponseEntity<Product> getSingleProduct(@RequestParam int productId){

        if (productId<0 ){
            throw new ApplicationException("Invalid Product Id!");
        }

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for this id :" + productId));

        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @PostMapping("/update/product/{id}")
    public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable int id) {
        try {
            Product existingProduct = productService.getProductById(id).get();
            product.setId(id);
            productService.saveOrUpdateProduct(product);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
