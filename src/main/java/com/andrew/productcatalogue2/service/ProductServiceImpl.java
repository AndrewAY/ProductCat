package com.andrew.productcatalogue2.service;

import com.andrew.productcatalogue2.entity.BookFormat;
import com.andrew.productcatalogue2.entity.Language;
import com.andrew.productcatalogue2.entity.MovieFormat;
import com.andrew.productcatalogue2.entity.Product;
import com.andrew.productcatalogue2.repository.BookFormatRepository;
import com.andrew.productcatalogue2.repository.LanguageRepository;
import com.andrew.productcatalogue2.repository.MovieFormatRepository;
import com.andrew.productcatalogue2.repository.ProductRepository;
import com.andrew.productcatalogue2.search.ProductSearchCriteria;
import com.andrew.productcatalogue2.search.ProductSearchResults;
import com.andrew.productcatalogue2.search.SortOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

//
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MovieFormatRepository movieFormatRepository;

    @Autowired
    private BookFormatRepository bookFormatRepository;

    @Autowired
    private ProductSearchCriteria origSearchCriteria;

    @Autowired
    private ProductSearchResults searchResults;

    private final SortOrder DEFAULT_SORT_ORDER = SortOrder.CREATED_AT_DESC;


    @Transactional
    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER')")
    public Product saveProduct(Product product) {
           return productRepository.save(product);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER')")
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }


    @Override
    public Optional<Product> findProduct(Long productId) {
        return productRepository.findById(productId);
    }



    public ProductSearchResults findProducts(ProductSearchCriteria searchCriteria, Pageable pageable) {


        ProductSearchCriteria.SearchBy searchBy = searchCriteria.getSearchBy();
        String productType = (searchCriteria.getProductType().isPresent())? searchCriteria.getProductType().get().toString() : "%";
        String searchTerm = searchCriteria.getSearchTerm();
        SortOrder sortOrder = (searchCriteria.getSortOrder().isPresent())? searchCriteria.getSortOrder().get() : DEFAULT_SORT_ORDER;

        origSearchCriteria.setSearchTerm(searchTerm);
        origSearchCriteria.setSortOrder(Optional.of(sortOrder));


        BigDecimal minPrice;
        if(!hasValidMinPrice(searchCriteria.getMinPrice())) {
            minPrice = BigDecimal.valueOf(0);
            origSearchCriteria.setMinPrice(Optional.empty());
        }
        else {
            minPrice = searchCriteria.getMinPrice().get().setScale(2, RoundingMode.CEILING);
            origSearchCriteria.setMinPrice(Optional.of(minPrice));
        }

        BigDecimal maxPrice;
        if(!hasValidMaxPrice(minPrice, searchCriteria.getMaxPrice())) {
            maxPrice = BigDecimal.valueOf(Double.MAX_VALUE);
            origSearchCriteria.setMaxPrice(Optional.empty());
        }
        else {
            maxPrice = searchCriteria.getMaxPrice().get().setScale(2, RoundingMode.CEILING);
            origSearchCriteria.setMaxPrice(Optional.of(maxPrice));
        }


         if(searchBy.equals(ProductSearchCriteria.SearchBy.TITLE)) {
             searchResults.setProductPage(fetchProductsFromRepository(sortOrder, productType, searchTerm, minPrice, maxPrice, pageable));
         }
         else
         if(searchBy.equals(ProductSearchCriteria.SearchBy.ID)) {
             List<Product> productList;

             if (isParseableLong(searchTerm) && !searchTerm.isEmpty()) {
                 /*
                  * if search by id is selected, we need to still return a List for the front end
                  * so create a List with just one product.
                  * Need to validate that searchTerm is parseable first, if not then don't add any product
                  * */
                 Long id = Long.parseLong(searchCriteria.getSearchTerm());

                 try {
                     productList = singletonList(findProduct(id).get());
                     Page<Product> productPage = new PageImpl(productList);
                     searchResults.setProductPage(productPage);
                 }
                 catch(NoSuchElementException e) {
                     //send back an empty List if no matching  product id
                     productList = emptyList();
                     Page<Product> productPage = new PageImpl(productList);
                     searchResults.setProductPage(productPage);
                 }

             }
             else {
                 //send back an empty List
                 productList = emptyList();
                 Page<Product> productPage = new PageImpl(productList);
                 searchResults.setProductPage(productPage);
             }

         }


        Long productCount = searchResults.getProductPage().getTotalElements();

        searchResults.setProductCount(productCount);
        searchResults.setSearchCriteria(origSearchCriteria);
        return searchResults;
    }




    private Page<Product> fetchProductsFromRepository(SortOrder sortOrderEnum, String productType, String searchTerm, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        Page<Product> fetchedProducts;
        switch(sortOrderEnum) {
            case PRICE_DESC:
                fetchedProducts = productRepository.productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByPriceDesc(productType,searchTerm, minPrice, maxPrice, pageable);
                break;
            case PRICE_ASC:
                fetchedProducts = productRepository.productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByPriceAsc(productType,searchTerm, minPrice, maxPrice, pageable);
                break;
            case CREATED_AT_DESC:
                fetchedProducts = productRepository.productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(productType,searchTerm, minPrice, maxPrice, pageable);
                break;
            case TITLE_ASC:
                fetchedProducts = productRepository.productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByTitleAsc(productType,searchTerm, minPrice, maxPrice, pageable);
                break;
            default:
                fetchedProducts = productRepository.productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByTitleAsc(productType,searchTerm, minPrice, maxPrice, pageable);
                break;
        }

        return fetchedProducts;
    }





    // will return false if there is no minPrice value set or if it's zero or less
    private boolean hasValidMinPrice(Optional<BigDecimal> minPrice) {

        if(!minPrice.isPresent()) {
            return false;
        }
        else if(minPrice.get().doubleValue() <= 0) {
            return false;
        }
        else
            return true;

}



    // will return false if no value set or if maxPrice < minPrice
    private boolean hasValidMaxPrice(BigDecimal minPrice, Optional<BigDecimal> maxPrice) {

        if(!maxPrice.isPresent()) {
            return false;
        }
        else if(maxPrice.get().compareTo(minPrice) < 0) {
            return false;
        }
        else
            return true;
    }


    public List<Language> getLanguages () {
        return convertToList(languageRepository.findAll());
    }

    public List<MovieFormat> getMovieFormats() {
        return convertToList(movieFormatRepository.findAll());
    }

    public List<BookFormat> getBookFormats() {
        return convertToList(bookFormatRepository.findAll());
    }

    private <T> List<T> convertToList(Iterable<T> iterable) {
       List<T> list = new ArrayList<T>();
       iterable.forEach(list::add);
       return list;
    }

    private boolean isParseableLong (String number) {
        try {
            Long.parseLong(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
