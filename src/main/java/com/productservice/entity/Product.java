package com.productservice.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productTitle;
    private String productSlug;
    private String productOverview;
    private String productDescription;
    private String[] productImages;
    private int categoryId;
    private double productOriginalPrice;
    private double productFinalPrice;
    private double productRating;
    private Date date;
    private int discountPercentage;
    


}
