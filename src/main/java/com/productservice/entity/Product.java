package com.productservice.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    private String productTitle;
    private String productSlug;
    private String productOverview;
    private String productDescription;
    @ElementCollection
    @CollectionTable(name = "product_images_table", joinColumns = @JoinColumn(name = "id"))
    private List<String> productImages;
    private int categoryId;
    private int sellerId;
    private double productOriginalPrice;
    private double productFinalPrice;
    private double productRating;
    private int discountPercentage;
    private String createdAt;
    private String updatedAt;


    @ManyToOne(targetEntity=Seller.class, cascade = CascadeType.MERGE)
    @JoinColumn(name="sellerId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Seller seller;


    


}
