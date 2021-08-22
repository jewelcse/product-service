package com.productservice.service;

import com.productservice.dto.ProductDto;
import com.productservice.entity.Product;
import com.productservice.exception.ApplicationException;
import com.productservice.repository.ProductRepository;
import com.productservice.util.MethodUtils;
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
import java.util.*;


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
    public Product saveOrUpdateProduct(ProductDto dto_product) throws IOException {
        //cachingService.evictAllCaches();
        Product db_product = new Product();
        db_product.setProductTitle(dto_product.getProductTitle());
        String ps = MethodUtils.toSlug(dto_product.getProductTitle());
        db_product.setProductSlug(ps);
        db_product.setProductOverview(dto_product.getProductOverview());
        db_product.setProductDescription(dto_product.getProductDescription());

        boolean doesExit = getProductByProductSlug(ps);
        if (doesExit){
            throw new ApplicationException(dto_product.getProductTitle() + "Already Exit ");
        }

        db_product.setProductOriginalPrice(dto_product.getProductOriginalPrice());
        db_product.setDiscountPercentage(dto_product.getDiscountPercentage());

        if (dto_product.getDiscountPercentage()> 0){
            double disAmount = (dto_product.getProductOriginalPrice()* dto_product.getDiscountPercentage())/100;
            db_product.setProductFinalPrice(dto_product.getProductOriginalPrice()-disAmount);
        }else{
            db_product.setProductFinalPrice(dto_product.getProductOriginalPrice());
        }

        db_product.setProductImages(saveImages(dto_product.getFiles()));

        db_product.setCategoryId(dto_product.getCategoryId());
        db_product.setSellerId(dto_product.getSellerId());
        db_product.setCreatedAt(MethodUtils.getLocalDateTime());
        db_product.setUpdatedAt(null);

        return productRepository.save(db_product);
    }

    private List<String> saveImages(MultipartFile[] files) throws IOException {
        List<String> fileNames = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file->{
            fileNames.add(saveFiles(file));
        });
        return fileNames;
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
