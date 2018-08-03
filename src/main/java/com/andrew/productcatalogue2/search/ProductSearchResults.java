package com.andrew.productcatalogue2.search;

import com.andrew.productcatalogue2.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


/**
 * Holds information relating to search results returned from
 * product search queries. Getters and setters are available for all fields.
 *
 * @author Andrew Au-Young
 * @Version 1.0.0
 */
@Component
@Data
@NoArgsConstructor
public class ProductSearchResults {

    /**total number of matching products found*/
    private Long productCount;
    /**the requested page of matching {@link Product} instances*/
    private Page<Product> productPage;
    /**a {@link ProductSearchCriteria} instances representing the actual search criteria values used to query the database*/
    private ProductSearchCriteria searchCriteria;
//    /**the requested {@link com.andrew.productcatalogue2.entity.Product.ProductType}*/
//    private Product.ProductType productType;


}
