package com.productservice.repository;

import com.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findAllByOrderByIdDesc();

    //@Query(value = "SELECT * FROM product WHERE productSlug := ps")
    Optional<Product> findByProductSlug(String ps);

    List<Product> findAllByCategoryId(int categoryId);
}
