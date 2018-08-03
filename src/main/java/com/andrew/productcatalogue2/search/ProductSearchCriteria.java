package com.andrew.productcatalogue2.search;

import com.andrew.productcatalogue2.entity.Product;
import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Optional;


/**
 * Holds various values regarding product search parameters that users request when
 * searching for products. A  ProductSearchCriteria instance is passed as a parameter
 * in the {@link com.andrew.productcatalogue2.service.ProductService#findProducts(ProductSearchCriteria, Pageable) findProducts}
 * method in {@link com.andrew.productcatalogue2.service.ProductService}.
 * <p>Getters and setters are available for all fields.</p>
 *
 * @author Andrew Au-Young
 * @Version 1.0.0
 */

@Component
@Data
@NoArgsConstructor
public class ProductSearchCriteria {

    /** Indicates whether to search by title or by id */
    public enum SearchBy {
        TITLE,ID;
    }

    /** the type/category of product*/
    private Optional<Product.ProductType> productType;

    /** the search term or id number to search for */
    private String searchTerm;

    /** instructs whether to search by title or id */
    private SearchBy searchBy;

   /** the order that the results should be arranged in */
    private Optional<SortOrder> sortOrder;

    /** the minimum price in the price range */
    private Optional<BigDecimal> minPrice;

    /** the maximum price in the price range */
    private Optional<BigDecimal> maxPrice;


}
