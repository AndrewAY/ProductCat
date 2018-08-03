package com.andrew.productcatalogue2.service;

import com.andrew.productcatalogue2.entity.BookFormat;
import com.andrew.productcatalogue2.entity.Language;
import com.andrew.productcatalogue2.entity.MovieFormat;
import com.andrew.productcatalogue2.entity.Product;
import com.andrew.productcatalogue2.search.ProductSearchCriteria;
import com.andrew.productcatalogue2.search.ProductSearchResults;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/** Handles transactions involving retrieving, updating, deleting or creating products and related data
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
public interface ProductService {

    /**
     * @param productId
     * @return the matching product (if it exists, otherwise {@link Optional#empty() Optional.empty()})
     */
    Optional<Product> findProduct(Long productId);
    Product saveProduct(Product product);

//     boolean updateProduct(Product product);
     void deleteProduct(Product product);

     List<Language> getLanguages();
     List<MovieFormat> getMovieFormats();
    List<BookFormat> getBookFormats();

    /**
     * Finds matching products based on the search criteria , and returns a page
     * of results based on the requested paging values.
     *
     * @param searchCriteria an instance of {@link ProductSearchCriteria} containing the search criteria
     * @param pageable an instance of {@link Pageable} containing the paging options
     * @return
     */
   ProductSearchResults findProducts(ProductSearchCriteria searchCriteria, Pageable pageable);

}
