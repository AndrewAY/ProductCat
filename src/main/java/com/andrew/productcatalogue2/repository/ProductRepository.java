package com.andrew.productcatalogue2.repository;


import com.andrew.productcatalogue2.entity.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
//@Profile("production")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    Page<Product> productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByPriceDesc(String productType,String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByPriceAsc(String productType,String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(String productType,String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByTitleAsc(String productType,String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

}
