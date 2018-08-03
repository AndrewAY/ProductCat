package com.andrew.productcatalogue2.service;

import com.andrew.productcatalogue2.entity.Movie;
import com.andrew.productcatalogue2.entity.Product;
import com.andrew.productcatalogue2.repository.ProductRepository;
import com.andrew.productcatalogue2.search.ProductSearchCriteria;
import com.andrew.productcatalogue2.search.ProductSearchResults;
import com.andrew.productcatalogue2.search.SortOrder;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import parameter_resolver.ProductParameterResolver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;



@ExtendWith(ProductParameterResolver.class)
@ExtendWith(SpringExtension.class)
@Slf4j
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductSearchCriteria searchCriteria;
    @Mock
    private ProductSearchResults searchResults;
    @InjectMocks
    private ProductServiceImpl productService;

    private Movie movie10;
    private List<Product> productList;
    private ProductSearchCriteria emptySearchCriteria;
    private Pageable defaultPageable;
    private Page<Product> someResultsPage;






    @BeforeEach
    void initBeforeEach(Map<String, Product> products){

        this.movie10 =  (Movie)products.get("movie10");
        productList = new ArrayList<Product>(products.values());
        someResultsPage = new PageImpl(productList, PageRequest.of(0, 10), productList.size());

        //Set null values for all optional SearchCriteria object attributes, and default values for the rest
        emptySearchCriteria = new ProductSearchCriteria();
        emptySearchCriteria.setSearchBy(ProductSearchCriteria.SearchBy.TITLE);
        emptySearchCriteria.setSearchTerm("");
        emptySearchCriteria.setProductType(Optional.empty());
        emptySearchCriteria.setSortOrder(Optional.empty());
        emptySearchCriteria.setMinPrice(Optional.empty());
        emptySearchCriteria.setMaxPrice(Optional.empty());

        defaultPageable = PageRequest.of(0,5);

    }




    @Nested
    @DisplayName("Finding single products")
    class FindingSingleProducts {
        @Test
        @DisplayName("valid id should return a product")
        public void whenValidId_thenProductShouldBeFound() {
            Long productId = 10L;

            //given
            given(productRepository.findById(productId)).willReturn(Optional.of(movie10));

            //when
            Product product = productService.findProduct(productId).get();

            //assert
            assertThat(product).isEqualTo(movie10);
            verify(productRepository).findById(productId);
        }


        @Test
        @DisplayName("Invalid id should not return any products")
        public void whenInvalidId_thenNoProductsFound() {
            //given
            given(productRepository.findById(10L)).willReturn(Optional.of(movie10));

            //when
            Long productId = 999L;
            Optional<Product> product = productService.findProduct(productId);

            //assert
            assertThat(product).isEqualTo(Optional.empty());
            verify(productRepository).findById(productId);

        }
    }






    @Nested
    @DisplayName("findProducts(ProductSearchCriteria searchCriteria, Pageable pageable) method tests")
    class FindProductsTests {



    @Test
    @DisplayName("should request to fetch products with requested search criteria values")
    void FindingProductsBasedOnSearchCriteria() {

        //setup some specific search criteria
        ProductSearchCriteria specificSearchCriteria = new ProductSearchCriteria();
        specificSearchCriteria.setSearchTerm("movie");
        specificSearchCriteria.setMinPrice(Optional.of(BigDecimal.valueOf(1L)));
        specificSearchCriteria.setMaxPrice(Optional.of(BigDecimal.valueOf(11L)));
        specificSearchCriteria.setSortOrder(Optional.of(SortOrder.CREATED_AT_DESC));
        specificSearchCriteria.setProductType(Optional.of(Product.ProductType.MOVIE));
        specificSearchCriteria.setSearchBy(ProductSearchCriteria.SearchBy.TITLE);


        //given
        given(productRepository.productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(
                        Product.ProductType.MOVIE.toString()
                        ,"movie"
                        , BigDecimal.valueOf(1L)
                        , BigDecimal.valueOf(11L)
                        ,  defaultPageable))
                .willReturn(someResultsPage);
        given(searchResults.getProductPage()).willReturn(someResultsPage);


        //when
        ProductSearchResults productSearchResults = productService.findProducts(specificSearchCriteria,defaultPageable);

        //assert
            assertThat(productSearchResults.getProductPage().getTotalElements()).isEqualTo(20);
            assertThat(productSearchResults.getProductPage()).isEqualTo(someResultsPage);

            //verify the method was called once (with how the min and max prices would be altered in ProductServiceImpl)
           verify(productRepository).productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(
                   Product.ProductType.MOVIE.toString()
                   ,"movie"
                   , BigDecimal.valueOf(1L).setScale(2, RoundingMode.CEILING)
                   , BigDecimal.valueOf(11L).setScale(2, RoundingMode.CEILING)
                   , defaultPageable);
    }




    @Test
    @DisplayName("should request to fetch all products if optional search criteria values are null or empty")

    void FindingProductsWhenNoOptionalSearchCriteriaIsRequested() {


        given(productRepository.productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(
                "%",
                "",
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(Double.MAX_VALUE),
                defaultPageable))
                .willReturn(someResultsPage);

        given(searchResults.getProductPage()).willReturn(someResultsPage);

        ProductSearchResults productSearchResults = productService.findProducts(emptySearchCriteria, defaultPageable);

        //assert
        verify(productRepository).productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(
                "%",
                "",
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(Double.MAX_VALUE),
                defaultPageable);

    }


        @Test
        @DisplayName("for productId searches, should return an empty List if invalidly formatted id")
        void invalidIdShouldReturnEmptyListForIdSearches() {

            Page<Product> resultsPage = new PageImpl(productList, PageRequest.of(0, 10), productList.size());

            ProductSearchCriteria thisSearchCriteria = emptySearchCriteria;
            thisSearchCriteria.setSearchBy(ProductSearchCriteria.SearchBy.ID);
            thisSearchCriteria.setSearchTerm("some non-numeric String value");
            Page<Product> emptyProductPage = new PageImpl(emptyList());

            //given
            given(searchResults.getProductPage()).willReturn(someResultsPage);

            //when
            productService.findProducts(thisSearchCriteria, defaultPageable);

            //assert
            verifyZeroInteractions(productRepository);
            verify(searchResults).setProductPage(emptyProductPage);
        }


        @Test
        @DisplayName("for productId searches, should query repository with correct id if correctly formatted")
        void validIdShouldTriggerCorrectRepositoryQuery() {
            ProductSearchCriteria thisSearchCriteria = emptySearchCriteria;
            thisSearchCriteria.setSearchBy(ProductSearchCriteria.SearchBy.ID);
            thisSearchCriteria.setSearchTerm("10");

            //given
            given(searchResults.getProductPage()).willReturn(someResultsPage);

            //when
            productService.findProducts(thisSearchCriteria, defaultPageable);

            //assert
            verify(productRepository).findById(10L);
        }


    } // end FindProductsTests


    @Nested
    @DisplayName("Validating min and max prices")
    class validatingMinAndMaxPrices {


        @Test
        @DisplayName("Negative values for minPrice should be corrected to zero when querying repository")
        public void shouldCorrectNegativelMinPriceValues() {

            ProductSearchCriteria searchCriteria = emptySearchCriteria;
            searchCriteria.setMinPrice(Optional.of(BigDecimal.valueOf(-5)));


            //given
            given(searchResults.getProductPage()).willReturn(someResultsPage);

            //when
            productService.findProducts(searchCriteria, defaultPageable);

            //assert
            verify(productRepository).productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(
                    "%",
                    "",
                    BigDecimal.valueOf(0),
                    BigDecimal.valueOf(Double.MAX_VALUE),
                    defaultPageable);
        }


        @Test
        @DisplayName("Negative values for maxPrice should be corrected to max BigDecimal value when querying repository")
        public void shouldCorrectNegativeMaxPriceValues() {

            ProductSearchCriteria searchCriteria = emptySearchCriteria;
            searchCriteria.setMaxPrice(Optional.of(BigDecimal.valueOf(-5)));


            //given
            given(searchResults.getProductPage()).willReturn(someResultsPage);

            //when
            ProductSearchResults productSearchResults = productService.findProducts(searchCriteria, defaultPageable);

            //assert
            verify(productRepository).productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(
                    "%",
                    "",
                    BigDecimal.valueOf(0),
                    BigDecimal.valueOf(Double.MAX_VALUE),
                    defaultPageable);



        }




        @Test
        @DisplayName("If max price is below min price, then should use a max BigDecimal value for max price when querying repository")
        public void correctMaxPriceIfLowerThanMinPrice() {

            ProductSearchCriteria searchCriteria = emptySearchCriteria;
            searchCriteria.setMinPrice(Optional.of(BigDecimal.valueOf(10)));
            searchCriteria.setMaxPrice(Optional.of(BigDecimal.valueOf(5)));


            //given
            given(searchResults.getProductPage()).willReturn(someResultsPage);

            //when
            productService.findProducts(searchCriteria, defaultPageable);

            //assert
            verify(productRepository).productTypeLikeAndTitleContainsIgnoringCaseAndPriceBetweenOrderByCreatedAtDesc(
                    "%",
                    "",
                    BigDecimal.valueOf(10L).setScale(2, RoundingMode.CEILING),
                    BigDecimal.valueOf(Double.MAX_VALUE),
                    defaultPageable);
        }

    } // end validatingMinAndMaxPrices




}
