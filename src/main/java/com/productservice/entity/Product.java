


package com.productservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(columnDefinition = "TEXT")
    private String productOverview;
    @Column(columnDefinition = "TEXT")
    private String productDescription;
    @ElementCollection(fetch = FetchType.EAGER)
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


    


}
