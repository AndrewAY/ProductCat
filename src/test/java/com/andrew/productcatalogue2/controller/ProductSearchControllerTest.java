package com.andrew.productcatalogue2.controller;


import com.andrew.productcatalogue2.entity.*;
import com.andrew.productcatalogue2.search.ProductSearchCriteria;
import com.andrew.productcatalogue2.search.ProductSearchResults;
import com.andrew.productcatalogue2.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import parameter_resolver.ProductParameterResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;



@ExtendWith(SpringExtension.class)
@ExtendWith(ProductParameterResolver.class)
@Slf4j
@WebMvcTest(controllers= ProductSearchController.class)
@AutoConfigureMockMvc(secure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductSearchControllerTest {
    private List<Product> productList;
    private Movie movie180;
    private Movie movie10;
    private Book book20;




    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;
    @MockBean
    private ProductSearchCriteria searchCriteria;
    @MockBean
    private ProductSearchResults productSearchResults;


    @BeforeAll
    void initBeforeAll(Map<String, Product> products){
        productList = new ArrayList<Product>(products.values());
        movie10 =  (Movie)products.get("movie10");
        book20 = (Book)products.get("book20");
    }



    @Nested
    @DisplayName("Display Product Information")
    class displayProductInformation {
        @Test
        @DisplayName("URL request for movie returns correct view and product")
         void shouldDisplayProductInformationForAMovie() throws Exception {

            //Given
            given(productService.findProduct(10L)).willReturn(Optional.of(movie10));

            //When  & assert
            mvc.perform(get("http://localhost:8080/product-cat/10")
                    .accept(MediaType.ALL))
                    .andExpect(status().isOk())
                    .andExpect(view().name("view-movie"))
                    .andExpect(model().attribute("product", movie10));
            verify(productService, times(1)).findProduct(10L);
        }


        @Test
        @DisplayName("URL request for book returns correct view and product")
        public void shouldDisplayCorrectViewAndProductForABook() throws Exception {

            //Given
            given(productService.findProduct(20L)).willReturn(Optional.of(book20));

            //When  & assert
            mvc.perform(get("http://localhost:8080/product-cat/20")
                    .accept(MediaType.ALL))
                    .andExpect(status().isOk())
                    .andExpect(view().name("view-book"))
                    .andExpect(model().attribute("product", book20));
            verify(productService, times(1)).findProduct(20L);
        }


        @Test
        @DisplayName("URL request for unrecognised product id returns error page")
        public void shouldDirectToErrorPageIfInvalidProductId() throws Exception {

            //Given
            given(productService.findProduct(999L)).willReturn(Optional.empty());

            //When & assert
            mvc.perform(get("http://localhost:8080/product-cat/999")
                    .accept(MediaType.ALL))
                    .andExpect(status().isOk())
                    .andExpect(view().name("/error/error"));

            verify(productService).findProduct(999L);
        }


    } //end Display Product Information



}
