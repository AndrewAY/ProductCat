package com.andrew.productcatalogue2.controller;

import com.andrew.productcatalogue2.entity.Book;
import com.andrew.productcatalogue2.entity.Movie;
import com.andrew.productcatalogue2.entity.Product;
import com.andrew.productcatalogue2.pagination.PagerModel;
import com.andrew.productcatalogue2.search.ProductSearchCriteria;
import com.andrew.productcatalogue2.search.ProductSearchResults;
import com.andrew.productcatalogue2.search.SortOrder;
import com.andrew.productcatalogue2.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@SessionAttributes({"searchResults","searchCriteria"})
@RequestMapping("/")
@Slf4j
public class ProductSearchController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = { 5, 10};
    private final String PRODUCT_IMAGE_URL;
    private final  String GENERAL_SEARCH_URL;
    private final  String ADMIN_SEARCH_URL;

    @Autowired
    ProductService productService;

    @Autowired
    ProductSearchCriteria searchCriteria;

    @Autowired
    ProductSearchResults productSearchResults;


    public ProductSearchController(@Value("${product.image.url}") String productImageURL,
                                @Value("${general-product-search-url}") String genSearchURL,
                                @Value("${admin-product-search-url}") String adminSearchURL){
        PRODUCT_IMAGE_URL = productImageURL;
        GENERAL_SEARCH_URL = genSearchURL;
        ADMIN_SEARCH_URL = adminSearchURL;
    }




    @GetMapping({"/product-search", "/admin/product-search"})
    public ModelAndView getProductListings( @RequestParam("productType") Optional<String> strProductType,
                                            @RequestParam("searchTerm") Optional<String> searchTerm,
                                            @RequestParam("sortOrder") Optional<SortOrder> sortOrder,
                                            @RequestParam("minPrice") Optional<BigDecimal> minPrice,
                                            @RequestParam("maxPrice") Optional<BigDecimal> maxPrice,
                                            @RequestParam("pageSize") Optional<Integer> pageSize,
                                            @RequestParam("page") Optional<Integer> page,
                                            @RequestParam("searchBy") Optional<String> searchBy,
                                            @ModelAttribute("popupMessage") Optional<String> popupMessage,
                                            HttpServletRequest request) {


        ModelAndView modelAndView = new ModelAndView("product-search");

        // check if any request for a popup msg
        String evalPopupMessage = popupMessage.orElse("");
        if(!evalPopupMessage.isEmpty()) {
            modelAndView.addObject("popupMessage", popupMessage);
        }


        //if requesting URI is from the admin zone then let the webpage know so it can include product management options
        String requestingURI = request.getRequestURI();
        if (requestingURI.equalsIgnoreCase("/admin/product-search")) {
            modelAndView.addObject("isAdmin", true);
            modelAndView.addObject("searchURL", ADMIN_SEARCH_URL);
        }
        else {
            modelAndView.addObject("searchURL", GENERAL_SEARCH_URL);
        }


        /*
        * Evaluate the requested params and make sure values are set.
        * to evaluate the productType, need to evaluate the String parameter first, then convert to a ProductType if 'ALL' wasn't selected'
        */

        String evalStrProductType = strProductType.orElse("ALL");
        Optional<Product.ProductType> evalProductType = Optional.empty();
        if (!evalStrProductType.equalsIgnoreCase("ALL")) {
            try {
                evalProductType = Optional.of(Product.ProductType.valueOf(evalStrProductType));
            }
            catch(Error e) {
                log.error("Error caught when trying to set ProductType", e);
            }
        }

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;


        //Evaluate the rest of the requested parameters
        String evalSearchTerm = searchTerm.orElse("");
        String evalSearchBy = searchBy.orElse("TITLE");
        Optional<SortOrder> evalSortOrder = (sortOrder.isPresent()) ? sortOrder : Optional.empty();
        Optional<BigDecimal> evalMinPrice = (minPrice.isPresent()) ? minPrice :  Optional.empty();
        Optional<BigDecimal> evalMaxPrice = (maxPrice.isPresent()) ? maxPrice :  Optional.empty();




        //store the search criteria info to send to productService.findProducts
        searchCriteria.setProductType(evalProductType);
        searchCriteria.setSearchTerm(evalSearchTerm);
        searchCriteria.setSearchBy(ProductSearchCriteria.SearchBy.valueOf(evalSearchBy));
        searchCriteria.setSortOrder(evalSortOrder);
        searchCriteria.setMinPrice(evalMinPrice);
        searchCriteria.setMaxPrice(evalMaxPrice);


        //  fetch results from service class
        if(searchCriteria.getSearchBy().equals(ProductSearchCriteria.SearchBy.TITLE)) {
            productSearchResults = productService.findProducts(searchCriteria, PageRequest.of(evalPage, evalPageSize));
        }
        else if(searchCriteria.getSearchBy().equals(ProductSearchCriteria.SearchBy.ID)) {
            productSearchResults = productService.findProducts(searchCriteria, PageRequest.of(evalPage, evalPageSize));
        }
        else {
            //default to search by title
            productSearchResults = productService.findProducts(searchCriteria, PageRequest.of(evalPage, evalPageSize));
        }


        // prepare the page and set the correct values for the PagerModel
        Page<Product> productPage = productSearchResults.getProductPage();
        PagerModel pager = new PagerModel(productPage.getTotalPages(), productPage.getNumber(), BUTTONS_TO_SHOW);



        modelAndView.addObject("productPage", productPage);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("productCount",  productSearchResults.getProductCount());
        modelAndView.addObject("productImagePath",PRODUCT_IMAGE_URL);
        modelAndView.addObject("selectedSearchBy",evalSearchBy);

        /*
        * I'm listing all the SearchCriteria attributes as separate model attributes (vs just using one
        * SearchCriteria object) because of problems when trying to map values on the html form directly to Optional
        * attributes in the SearchCriteria class using Thymeleaf
        * */
        modelAndView.addObject("minPrice", productSearchResults.getSearchCriteria().getMinPrice().orElse(null));
        modelAndView.addObject("maxPrice", productSearchResults.getSearchCriteria().getMaxPrice().orElse(null));
        modelAndView.addObject("searchTerm", productSearchResults.getSearchCriteria().getSearchTerm());


        //need these to populate the SortOrder and ProductType select inputs with values and ensure the previously requested one stays selected
        SortOrder selectedSortOrder = productSearchResults.getSearchCriteria().getSortOrder().get();
        EnumSet<SortOrder> sortOrderValues = EnumSet.allOf(SortOrder.class);
        modelAndView.addObject("sortOrderValues", sortOrderValues);
        modelAndView.addObject("selectedSortOrderValue", selectedSortOrder);
        EnumSet<Product.ProductType> productTypeValues = EnumSet.allOf(Product.ProductType.class);
        modelAndView.addObject("productTypes", productTypeValues);
        modelAndView.addObject("selectedProductType", evalStrProductType );

        return modelAndView;
    }






    //displaying single product page for a product
    @GetMapping("product-cat/{productId}")
    public ModelAndView displayProductInformation(@PathVariable("productId") long productId,
                                                  Optional<Boolean> enableBackButton,
                                                  HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();



        Boolean evalEnableBackButton = enableBackButton.orElse(false);
        if(evalEnableBackButton) {
            modelAndView.addObject("enableBackButton", evalEnableBackButton);
            modelAndView.addObject("backButtonText",  "Go Back");
            String referer = request.getHeader("Referer");
            if(!referer.isEmpty()) {
                modelAndView.addObject("backButtonURL", referer);
            }
        }

        Optional<Product> product = productService.findProduct(productId);
        if(product.isPresent()) {

            switch (product.get().getProductType()) {
                case MOVIE:
                    modelAndView.setViewName("view-movie");
//                Movie movie = (Movie)product;
                    modelAndView.addObject("product", (Movie) product.get());
                    break;
                case BOOK:
                    modelAndView.setViewName("view-book");
                    modelAndView.addObject("product", (Book) product.get());
                    break;
                default:
                    log.warn("product has no valid productType");
            }
        }
        else {
            log.info("no product with id:" + productId);
            modelAndView.setViewName("/error/error");
            modelAndView.addObject("message", "Unable to find a product with product Id: " + productId);
        }

        modelAndView.addObject("productImagePath", PRODUCT_IMAGE_URL);
        return modelAndView;
    }


}
