package com.andrew.productcatalogue2.controller;

import com.andrew.productcatalogue2.entity.*;
import com.andrew.productcatalogue2.exceptions.FileUploadException;
import com.andrew.productcatalogue2.service.FileUploadService;
import com.andrew.productcatalogue2.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/admin")
public class ProductManagementController {


    @Autowired
    private ProductService productService;
    @Autowired
    private FileUploadService fileUploadService;

    private final String PRODUCT_IMAGE_URL;
    private final String ADMIN_PRODUCT_SEARCH_URL;
    private List<Language> availableLanguages;
    private List<MovieFormat> availableMovieFormats;
    private List<BookFormat> availableBookFormats;


    public ProductManagementController(@Value("${product.image.url}") String productImageURL,
                                       @Value("${admin-product-search-url}") String adminSearchURL) {
        this.PRODUCT_IMAGE_URL = productImageURL;
        ADMIN_PRODUCT_SEARCH_URL = adminSearchURL;
    }



 /*
 *********** METHODS FOR FOR ADDING PRODUCTS ********************************************
 */


    /* entry menu to add a product - will refer to the appropriate address for the different add product forms */
    @GetMapping("/add-product")
    public ModelAndView handleAddProductMenu(@RequestParam("productType") Optional<Product.ProductType> productType) {

        ModelAndView modelAndView = new ModelAndView();

        // if there is a productType parameter value, it means that user has already selected which product type they want to add
        if(productType.isPresent()) {
                switch(productType.get()) {
                    case MOVIE:
                        modelAndView.setViewName("redirect:add-movie");
                        break;

                    case BOOK:
                        modelAndView.setViewName("redirect:add-book");
                        break;
                }
            }

        //... else display the add-product menu so user can select a product type
        else {
            modelAndView.setViewName("add-product-menu");
            EnumSet<Product.ProductType> productTypeValues = EnumSet.allOf(Product.ProductType.class);
            modelAndView.addObject("productTypes", productTypeValues);
        }

        return modelAndView;
    }




    @GetMapping("/add-movie")
    public ModelAndView addMovieFormPt1(Model model) {
        ModelAndView modelAndView = new ModelAndView("add-or-edit-movie-form");
        modelAndView.addObject("movie", new Movie());
        availableLanguages = productService.getLanguages();
        modelAndView.addObject("availableLanguages" , availableLanguages);
        availableMovieFormats = productService.getMovieFormats();
        modelAndView.addObject("availableFormats" , availableMovieFormats);
        modelAndView.addObject("actionURL" , "add-movie");
        modelAndView.addObject("action" , "add");
        return modelAndView;
    }


    @PostMapping("/add-movie")
    public String orighandleAddMovie(@Valid Movie movie, BindingResult result, Model model) {

        if(result.hasErrors()) {
            model.addAttribute("availableLanguages" , availableLanguages);
            model.addAttribute("availableFormats" , availableMovieFormats);
            model.addAttribute("actionURL" , "add-movie");
            model.addAttribute("action" , "add");
            return "add-or-edit-movie-form";
        }

//        Long productId =  productService.addProductAndReturnId(movie);
        Product product = productService.saveProduct(movie);
        return "redirect:/admin/add-product-image/" + product.getProductId() + "?action=add";

    }



    @GetMapping("/add-book")
    public ModelAndView addBookFormPt1( ) {

        ModelAndView modelAndView = new ModelAndView("add-or-edit-book-form");
        modelAndView.addObject("book" , new Book());
        availableLanguages = productService.getLanguages();
        modelAndView.addObject("availableLanguages" , availableLanguages);
        availableBookFormats = productService.getBookFormats();
        modelAndView.addObject("availableFormats" , availableBookFormats);
        modelAndView.addObject("actionURL" , "/admin/add-book");
        modelAndView.addObject("action" , "add");
        return modelAndView;
    }




    @PostMapping("/add-book")
    public String origHandleAddBook(@Valid Book book, BindingResult result, Model model) {

        if(result.hasErrors()) {
            model.addAttribute("availableLanguages" , availableLanguages);
            model.addAttribute("availableFormats" , availableBookFormats);
            model.addAttribute("actionURL" , "/admin/add-book");
            model.addAttribute("action" , "add");
            return "add-or-edit-book-form";
        }

        Product product = productService.saveProduct(book);
        return "redirect:/admin/add-product-image/" + product.getProductId() + "?action=add";

    }

    /*
     *********** METHODS FOR FOR EDITING PRODUCTS ********************************************
     */


    /* will refer to the appropriate address for the different EDIT product forms */
    @GetMapping("/edit-product")
    public String editProductForm(@RequestParam("productId") Long productId,
                                  @RequestParam("productType") Product.ProductType productType) {

        switch(productType) {
            case MOVIE:
                return "redirect:edit-movie/" + productId;


            case BOOK:
                return "redirect:edit-book/" + productId;

            default:
                log.warn("Error in productType value",new IllegalArgumentException("Invalid productType" + productType.toString()));
                return "redirect:/error";
        }

    }


    @GetMapping("/edit-movie/{productId}")
    public ModelAndView editMovieFormPt1(@PathVariable("productId") Long productId ) {

    ModelAndView modelAndView = new ModelAndView();
    Optional<Product> product = productService.findProduct(productId);

    if(!product.isPresent()) {
        modelAndView.setViewName("error");
        modelAndView.addObject("message", "Unable to find a product with product Id: " + productId );
        return modelAndView;
    }

        modelAndView.setViewName("add-or-edit-movie-form");
        modelAndView.addObject("movie", (Movie)product.get());
        availableLanguages = productService.getLanguages();
        modelAndView.addObject("availableLanguages" , availableLanguages);
        availableMovieFormats = productService.getMovieFormats();
        modelAndView.addObject("availableFormats" , availableMovieFormats);
        modelAndView.addObject("actionURL" , "/admin/edit-movie");
        modelAndView.addObject("action" , "edit");
        return modelAndView;
    }


    @PostMapping("/edit-movie")
    public String handleEditMovie(@Valid Movie movie, BindingResult result, Model model) {

        if(result.hasErrors()) {
            model.addAttribute("availableLanguages" , availableLanguages);
            model.addAttribute("availableFormats" , availableMovieFormats);
            model.addAttribute("actionURL" , "edit-movie");
            model.addAttribute("action" , "edit");
            return "add-or-edit-movie-form";
        }

//        Long productId =  productService.addProductAndReturnId(movie);
        Product product = productService.saveProduct(movie);
        return "redirect:/admin/add-product-image/" + product.getProductId() + "?action=edit";

    }



    @GetMapping("/edit-book/{productId}")
    public ModelAndView editBookFormPt1(@PathVariable("productId") Long productId ) {


        ModelAndView modelAndView = new ModelAndView();
        Optional<Product> product = productService.findProduct(productId);

        if(!product.isPresent()) {
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "Unable to find a product with product Id: " + productId );
            return modelAndView;
        }

        modelAndView.setViewName("add-or-edit-book-form");
        modelAndView.addObject("book", (Book)product.get());
        availableLanguages = productService.getLanguages();
        modelAndView.addObject("availableLanguages" , availableLanguages);
        availableBookFormats = productService.getBookFormats();
        modelAndView.addObject("availableFormats" , availableBookFormats);
        modelAndView.addObject("actionURL" , "/admin/edit-book");
        modelAndView.addObject("action" , "edit");
        return modelAndView;
    }



    @PostMapping("/edit-book")
    public String handleEditBook(@Valid Book book, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("availableLanguages" , availableLanguages);
            model.addAttribute("availableFormats" , availableBookFormats);
            model.addAttribute("actionURL" , "edit-book");
            model.addAttribute("action" , "edit");
            return "add-or-edit-book-form";
        }

//        Long productId =  productService.addProductAndReturnId(book);
        Product product = productService.saveProduct(book);
        return "redirect:/admin/add-product-image/" + product.getProductId() + "?action=edit";
    }


    /*
     *********** METHOD FOR FOR DELETING PRODUCTS ********************************************
     */



    @GetMapping("delete-product")
    public ModelAndView deleteProductRequest(@RequestParam("productId") Long productId,
                                             HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        Optional<Product> product = productService.findProduct(productId);

        if(!product.isPresent()) {
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "Unable to find a product with product Id: " + productId );
            return modelAndView;
        }

        productService.deleteProduct(product.get());

        // refer back to product search with a pop up message
        String referer = request.getHeader("Referer");
        if(!referer.isEmpty()) {
            modelAndView.setViewName("redirect:" + referer);
            modelAndView.addObject("popupMessage", "Product has been deleted");
        }
        else {
            modelAndView.setViewName("redirect:/admin/product-search");
        }

        return modelAndView;
    }






    /*
     **************************** METHODS TO UPLOAD A PRODUCT IMAGE ********************************************
     * ** used after first phases of adding or editing products. Will redirect to a confirmation page after uploading
     */


    @GetMapping("/add-product-image/{productId}")
    public ModelAndView addProductImageForm (@PathVariable("productId") long productId,
                                             @RequestParam("action") String action) {

        ModelAndView modelAndView = new ModelAndView("add-product-image");

        Product product= productService.findProduct(productId).get();
        modelAndView.addObject("product", product);

        modelAndView.addObject("action", action); //eg. action=add or edit
        modelAndView.addObject("productImagePath", PRODUCT_IMAGE_URL);

        return modelAndView;
    }




    //will redirect to confirmation page passing on attributes including a Product instance
    @PostMapping("/add-product-image/{productId}")
    public RedirectView doAddProductImage (@PathVariable("productId") Long productId,
                                           @RequestParam("action") String action,
                                           @RequestParam MultipartFile file,
                                           RedirectAttributes attributes) {


        Product product = productService.findProduct(productId).get();

        if(file.isEmpty()) {
            log.info("no file to upload received");
            //do nothing
        }
        else {
            //attempt to upload file
            boolean successfulUpload = fileUploadService.uploadImage(file,productId);

            if (successfulUpload) {
                product.setImageName(productId.toString() + ".jpg");
//                productService.updateProduct(product);
                productService.saveProduct(product);
            }
            else {
                attributes.addFlashAttribute("error", "There was a problem uploading the selected image");
                log.warn("there was an error uploading an image", new FileUploadException("Unable to upload image"));
            }
        }

        if(action.equalsIgnoreCase("add")) {
            attributes.addAttribute("action", "add");
        }
        else if (action.equalsIgnoreCase("edit")) {
            attributes.addAttribute("action", "edit");
        }

        attributes.addFlashAttribute("product", product);
        return new RedirectView("/admin/update-confirmation");
    }



    /*uses the passed in Product instance from the referring controller method*/
    @GetMapping("/update-confirmation")
    public ModelAndView addOrEditProductConfirmation (@ModelAttribute("error") String error,
                                                      @ModelAttribute("product") Product product,
                                                      @RequestParam("action") String action){


        ModelAndView modelAndView = new ModelAndView();

        switch(action) {
            case "add":
                modelAndView.addObject("message", "The new product has been created.");
                break;
            case "edit":
                modelAndView.addObject("message", "The product has been updated.");
                break;
        }


        switch(product.getProductType()) {
            case MOVIE:
                modelAndView.setViewName("view-movie");
            // need to retrieve a fresh casted product to avoid problems with lazily initializing the Language list
                Movie movie = (Movie)productService.findProduct(product.getProductId()).get();
                modelAndView.addObject("product", movie);
                break;
            case BOOK:
                modelAndView.setViewName("view-book");
                break;
            default:
                Error e = new Error("product has no valid productType");
                log.warn("product has no valid productType", e);
        }

        modelAndView.addObject("productImagePath", PRODUCT_IMAGE_URL);
        modelAndView.addObject("enableBackButton", true);
        modelAndView.addObject("backButtonURL",  ADMIN_PRODUCT_SEARCH_URL);
        modelAndView.addObject("backButtonText", "Back to Product Search");
        return modelAndView;
    }


}
