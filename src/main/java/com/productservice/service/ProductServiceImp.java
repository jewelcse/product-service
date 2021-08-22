package com.productservice.service;

import com.productservice.entity.Product;
import com.productservice.exception.ApplicationException;
import com.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImp  implements ProductService{


    private final Path root = Paths.get("uploads");

    private ProductRepository productRepository;
    private CachingService cachingService;

    @Autowired
    public ProductServiceImp(ProductRepository repository,CachingService service){
        this.productRepository = repository;
        this.cachingService = service;
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            System.out.println(this.root.resolve(file.getOriginalFilename())+" save");
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
    @Override
    public String saveFiles(MultipartFile file){
        try {
            long timeInMillis = new Date().getTime();
            String filename = file.getOriginalFilename().toLowerCase().replaceAll(" ","_");
            String finalFileName = timeInMillis+"_"+filename;
            Files.copy(file.getInputStream(), this.root.resolve(finalFileName));
            return finalFileName;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }
    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


    @Override
    public Product saveOrUpdateProduct(Product product) {
        //cachingService.evictAllCaches();
        return productRepository.save(product);
    }

    @Override
    public void removeProduct(Product product){


        product.getProductImages().forEach(image->{
            try {
                Files.delete(Paths.get("uploads/"+image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

         productRepository.delete(product);
         //cachingService.evictAllCaches();
    }

    @Override
    public void removeProduct(int productId){

        Product product = productRepository.findById(productId).get();

        if (product == null){
            throw new ApplicationException("Product Not Found for Id: " + productId);
        }

        product.getProductImages().forEach(image->{
            try {
                Files.delete(Paths.get("uploads/"+image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        productRepository.delete(product);
        //cachingService.evictAllCaches();
    }

    @Override
    public boolean getProductByProductSlug(String ps) {
        Optional<Product> product =  productRepository.findByProductSlug(ps);
        if (product.isPresent()) return true;
        return false;
    }

    @Override
    public List<Product> getCategoryProducts(int categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<Product> getProducts() {
        System.out.println("Fetching Data from Database");
        return  productRepository.findAll();
    }

    @Override
    public List<Product> getProductsWithSorting(String field) {
        return  productRepository.findAll(Sort.by(Sort.Direction.DESC,field));
    }

    @Override
    public Page<Product> getProductsWithPagination(int offset,int pageSize) {
        return  productRepository.findAll(PageRequest.of(offset,pageSize));
    }



    @Override
    public Optional<Product> getProductById(int productId) {
        return productRepository.findById(productId);
    }
}
