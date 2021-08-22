package com.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String productTitle;
    private String productOverview;
    private String productDescription;
    private MultipartFile[] files;
    private int categoryId;
    private int sellerId;
    private double productOriginalPrice;
    private double productFinalPrice;
    private int discountPercentage;

}
