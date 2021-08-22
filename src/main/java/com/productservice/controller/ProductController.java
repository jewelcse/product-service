package com.productservice.controller;

import com.productservice.dto.ProductDto;
import com.productservice.entity.Product;
import com.productservice.exception.ApplicationException;
import com.productservice.exception.ProductNotFoundException;
import com.productservice.service.ProductService;
import com.productservice.util.JsonResponseEntityModel;
import com.productservice.util.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/api/v1/product-service/")
@CrossOrigin
public class ProductController {


    private ProductService productService;
    JsonResponseEntityModel<Object> responseEntityModel = new JsonResponseEntityModel<>();

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @PostMapping(path = "/product/create",consumes = {"multipart/form-data"})//consumes = {"multipart/form-data"}
    public ResponseEntity<JsonResponseEntityModel<Object>> createProduct(@ModelAttribute ProductDto dto_product) throws IOException {


        if (dto_product.getProductTitle().isEmpty() || dto_product.getProductTitle() == null){
            throw new ApplicationException("Product Title can't be Empty");
        }
        if (dto_product.getProductDescription().isEmpty() || dto_product.getProductDescription() == null){
            throw new ApplicationException("Product Description can't be Empty");
        }
        if (dto_product.getProductOverview().isEmpty() || dto_product.getProductOverview() == null){
            throw new ApplicationException("Product Overview can't be Empty");
        }
        if (dto_product.getCategoryId() <=0){
            throw new ApplicationException("Product Invalid Category Id");
        }
        if (dto_product.getProductOriginalPrice()<=0){
            throw new ApplicationException("Product Price can't be Empty");
        }
        if (dto_product.getSellerId()<=0){
            throw new ApplicationException("Seller Info can't be Empty");
        }
        responseEntityModel.setSuccess(true);
        responseEntityModel.setData(productService.saveOrUpdateProduct(dto_product));
        responseEntityModel.setStatusCode("201");
        return new ResponseEntity<JsonResponseEntityModel<Object>>(responseEntityModel,HttpStatus.CREATED);
    }





    @GetMapping("/uploads/view/{filename:.+}")
    public byte[] getSingleImg(@PathVariable String filename, HttpServletRequest request) throws IOException {
        File serverFile = new File("uploads/" + filename);
        if(serverFile.exists()){
            return Files.readAllBytes(serverFile.toPath());
        }
        return null;
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getPath(@PathVariable String filename, HttpServletRequest request){
        Resource resource = productService.load(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            //System.out.println(resource.getFile().getAbsolutePath() + " content");
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        //System.out.println(resource.getFilename() + " resoyrce");
        return ResponseEntity.ok() .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);


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

    @GetMapping("/get/products")
    @Cacheable(value = "products")
    public ResponseEntity<JsonResponseEntityModel<Object>> getAllProduct(){
        responseEntityModel.setSuccess(true);
        responseEntityModel.setData(productService.getProducts());
        responseEntityModel.setStatusCode("200");
        return new ResponseEntity<>(responseEntityModel,HttpStatus.OK);
    }

    @GetMapping("/get/products/{field}")
    public ResponseEntity<JsonResponseEntityModel<Object>> getAllProductWithSorting(@PathVariable String field){
        responseEntityModel.setSuccess(true);
        List<Product> data = productService.getProductsWithSorting(field);
        responseEntityModel.setData(data);
        responseEntityModel.setDataSize(data.size());
        responseEntityModel.setStatusCode("200");
        return new ResponseEntity<>(responseEntityModel,HttpStatus.OK);
    }

    @GetMapping("/get/products/{offset}/{pageSize}")
    public ResponseEntity<JsonResponseEntityModel<Object>> getAllProductWithPagination(
            @PathVariable int offset,
            @PathVariable int pageSize){
        responseEntityModel.setSuccess(true);
        Page<Product> data = productService.getProductsWithPagination(offset,pageSize);
        responseEntityModel.setData(data);
        responseEntityModel.setDataSize(data.getSize());
        responseEntityModel.setStatusCode("200");
        return new ResponseEntity<>(responseEntityModel,HttpStatus.OK);
    }




    @GetMapping("/get/products/byCategory/{categoryId}")
    public ResponseEntity<JsonResponseEntityModel<Object>> getCategoryProducts(@PathVariable int categoryId){
        responseEntityModel.setSuccess(true);
        responseEntityModel.setData(productService.getCategoryProducts(categoryId));
        responseEntityModel.setStatusCode("200");
        return new ResponseEntity<>(responseEntityModel,HttpStatus.OK);
    }

    @GetMapping("/get/product")
    public ResponseEntity<JsonResponseEntityModel<Object>> getSingleProduct(@RequestParam int productId){

        if (productId<0 ){
            throw new ApplicationException("Invalid Product Id!");
        }
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for this id :" + productId));
        responseEntityModel.setSuccess(true);
        responseEntityModel.setData(product);
        responseEntityModel.setStatusCode("200");
        return new ResponseEntity<JsonResponseEntityModel<Object>>(responseEntityModel,HttpStatus.OK);
    }

    @PostMapping("/update/product/{id}")
    public ResponseEntity<JsonResponseEntityModel<Object>> update(@RequestBody ProductDto product, @PathVariable int id) {

        Product exitingProduct = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for this id :" + id));
//
//            product.setId(exitingProduct.getId());
//            if (product.getProductTitle().isEmpty() || product.getProductTitle()==null){
//                product.setProductTitle(exitingProduct.getProductTitle());
//                product.setProductSlug(exitingProduct.getProductSlug());
//            }else {
//                product.setProductSlug(MethodUtils.toSlug(product.getProductTitle()));
//            }
//            if (product.getCategoryId() <=0){
//                product.setCategoryId(exitingProduct.getCategoryId());
//            }
//
//            product.setProductSlug(MethodUtils.toSlug(product.getProductTitle()));
//            product.setCreatedAt(exitingProduct.getCreatedAt());
//            product.setUpdatedAt(MethodUtils.getLocalDateTime());
//            responseEntityModel.setSuccess(true);
//            responseEntityModel.setData(productService.saveOrUpdateProduct(product));
//            responseEntityModel.setStatusCode("200");
            return new ResponseEntity<JsonResponseEntityModel<Object>>(responseEntityModel,HttpStatus.OK);

    }


}
