package com.productservice.controller;

import com.productservice.entity.Product;
import com.productservice.exception.ApplicationException;
import com.productservice.exception.ProductNotFoundException;
import com.productservice.service.ProductServiceImp;
import com.productservice.util.JsonResponseEntityModel;
import com.productservice.util.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api/v1/product-service/")
public class ProductController {

    private ProductServiceImp productService;
    JsonResponseEntityModel responseEntityModel = new JsonResponseEntityModel();

    @Autowired
    public ProductController(ProductServiceImp productService){
        this.productService = productService;
    }


    //@RabbitListener(queues = ProductConfig.PRODUCT_QUEUE)
    @PostMapping("/product/create")
    public ResponseEntity <JsonResponseEntityModel> createProduct(@RequestBody Product product) throws RuntimeException{

        if (product.getProductTitle().isEmpty() || product.getProductTitle() == null){
            throw new ApplicationException("Product Title can't be Empty");
        }

        if (product.getProductDescription().isEmpty() || product.getProductDescription() == null){
            throw new ApplicationException("Product Description can't be Empty");
        }

        if (product.getProductOverview().isEmpty() || product.getProductOverview() == null){
            throw new ApplicationException("Product Overview can't be Empty");
        }

        if (product.getCategoryId() <=0){
            throw new ApplicationException("Product Invalid Category Id");
        }

        if (product.getProductImages().length==0){
            throw new ApplicationException("Add at least one Image");
        }

        if (product.getProductOriginalPrice()<=0){
            throw new ApplicationException("Product Price can't be Empty");
        }
        if (product.getSellerId()<=0){
            throw new ApplicationException("Seller Info can't be Empty");
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
        product.setCreatedAt(MethodUtils.getLocalDateTime());
        product.setUpdatedAt(null);

        System.out.println(product);
        responseEntityModel.setSuccess(true);
        responseEntityModel.setData(productService.saveOrUpdateProduct(product));
        responseEntityModel.setStatusCode("201");

        return new ResponseEntity<>(responseEntityModel,HttpStatus.CREATED);
    }
    /*
     Admin Endpoint for remove product
     */
    @DeleteMapping("/remove/product")
    public ResponseEntity<String> removeProduct(@RequestParam int id){

        if (id<0 ){
            throw new ApplicationException("Invalid Product Id!");
        }

        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for this id :: " + id));

        productService.removeProduct(product);

        return new ResponseEntity<>(MethodUtils.prepareSuccessJSON(HttpStatus.OK,"Product Deleted Successfully"),HttpStatus.OK);

    }

    /*
    User and Admin endpoint for get all product list
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/get/products")
    @Cacheable(value = "products")
    public ResponseEntity<JsonResponseEntityModel> getAllProduct(){
        responseEntityModel.setSuccess(true);
        responseEntityModel.setData(productService.getProducts());
        responseEntityModel.setStatusCode("200");
        return new ResponseEntity<>(responseEntityModel,HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/get/product")
    public ResponseEntity<JsonResponseEntityModel> getSingleProduct(@RequestParam int productId){

        if (productId<0 ){
            throw new ApplicationException("Invalid Product Id!");
        }

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for this id :" + productId));

        responseEntityModel.setSuccess(true);
        responseEntityModel.setData(product);
        responseEntityModel.setStatusCode("200");
        return new ResponseEntity<>(responseEntityModel,HttpStatus.OK);
    }

    @PostMapping("/update/product/{id}")
    public ResponseEntity<JsonResponseEntityModel> update(@RequestBody Product product, @PathVariable int id) {

        Product exitingProduct = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for this id :" + id));

            product.setId(exitingProduct.getId());
            if (product.getProductTitle().isEmpty() || product.getProductTitle()==null){
                product.setProductTitle(exitingProduct.getProductTitle());
                product.setProductSlug(exitingProduct.getProductSlug());
            }else {
                product.setProductSlug(MethodUtils.toSlug(product.getProductTitle()));
            }
            if (product.getCategoryId() <=0){
                product.setCategoryId(exitingProduct.getCategoryId());
            }

            product.setProductSlug(MethodUtils.toSlug(product.getProductTitle()));
            product.setCreatedAt(exitingProduct.getCreatedAt());
            product.setUpdatedAt(MethodUtils.getLocalDateTime());
            responseEntityModel.setSuccess(true);
            responseEntityModel.setData(productService.saveOrUpdateProduct(product));
            responseEntityModel.setStatusCode("200");
            return new ResponseEntity<>(responseEntityModel,HttpStatus.OK);

    }


}
